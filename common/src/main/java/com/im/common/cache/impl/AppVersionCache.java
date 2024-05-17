package com.im.common.cache.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.cache.base.BaseCacheHandler;
import com.im.common.cache.base.CacheProperty;
import com.im.common.constant.RedisKeyEnum;
import com.im.common.entity.AppVersion;
import com.im.common.entity.Bank;
import com.im.common.entity.enums.AppTypeEnum;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.service.AppVersionService;
import com.im.common.service.BankService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.redis.RedisKeySerializer;
import com.im.common.util.redis.RedisValueSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * app版本管理缓存
 */
@CacheProperty(value = SysCacheRefreshTypeEnum.APP_VERSION, redis = true, local = false)
@Component
public class AppVersionCache implements BaseCacheHandler {
    private AppVersionService appVersionService;
    private ValueOperations<String, String> valueOperations;

    @Autowired
    public void setAppVersionService(AppVersionService appVersionService) {
        this.appVersionService = appVersionService;
    }

    @Autowired
    public void setValueOperations(ValueOperations<String, String> valueOperations) {
        this.valueOperations = valueOperations;
    }

    @Override
    public void reloadRedis() {
        loadAndCache();
    }

    /**
     * 查询所有app应用列表
     *
     * @return List<AppVersion>
     */
    public List<AppVersion> listFromRedis() {
        String str = valueOperations.get(RedisKeyEnum.APP_VERSION.getVal());
        if (StrUtil.isBlank(str)) {
            return new ArrayList<>();
        }

        return JSON.parseArray(str, AppVersion.class);
    }

    /**
     * 查询最新版本信息
     *
     * @param appType 应用类型
     * @return AppVersion
     */
    public AppVersion queryLatest(AppTypeEnum appType) {
        List<AppVersion> appVersions = listFromRedis();
        if(CollectionUtil.isEmpty(appVersions)){
            return null;
        }
        List<AppVersion> collect = appVersions.stream()
                .filter(p -> p.getAppType().equals(appType))
                .sorted(Comparator.comparing(AppVersion::getVersionCode).reversed())
                .collect(Collectors.toList());

        return CollectionUtil.isNotEmpty(collect)?collect.get(0):null;
    }

    /**
     * 加载并缓存数据
     */
    private void loadAndCache() {
        List<AppVersion> list = appVersionService.lambdaQuery().orderByDesc(AppVersion::getVersionCode).list();
        if(CollectionUtil.isNotEmpty(list)){
            valueOperations.set(RedisKeyEnum.APP_VERSION.getVal(),JSON.toJSONString(list));
        }
    }

}
