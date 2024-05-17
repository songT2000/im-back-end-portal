package com.im.common.cache.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.cache.base.BaseCacheHandler;
import com.im.common.cache.base.CacheProperty;
import com.im.common.constant.RedisKeyEnum;
import com.im.common.entity.I18nLanguage;
import com.im.common.entity.SysNotice;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.service.I18nLanguageService;
import com.im.common.service.SysNoticeService;
import com.im.common.util.CollectionUtil;
import com.im.common.vo.SysNoticePortalVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 公告缓存
 *
 * @author max.stark
 * @date 2021-01-26
 */
@CacheProperty(value = SysCacheRefreshTypeEnum.SYS_NOTICE, redis = true, local = false)
@Component
public class SysNoticeCache implements BaseCacheHandler {
    private SysNoticeService sysNoticeService;
    private ListOperations<String, String> listOperations;
    private RedisTemplate redisTemplate;
    private I18nLanguageService i18nLanguageService;


    @Override
    public void reloadRedis() {
        loadAndCache(this::resolveListForRedis);
    }

    /**
     * 查询所有公告
     * @param languageCode
     *
     * @return List<SysNoticePortalVO>
     */
    public List<SysNoticePortalVO> listFromRedis(String languageCode) {
        List<String> stringList = listOperations.range(formatCodeKey(languageCode), 0, -1);
        if (CollectionUtil.isEmpty(stringList)) {
            return new ArrayList<>();
        }
        return CollectionUtil.toList(stringList, s -> JSONObject.parseObject(s, SysNoticePortalVO.class));
    }

    /**
     * 查询公告分页
     * @param languageCode
     *
     * @return List<SysNoticePortalVO>
     */
    public List<SysNoticePortalVO> pageFromRedis(String languageCode, long start, long end) {
        List<String> stringList = listOperations.range(formatCodeKey(languageCode), start, end);
        if (CollectionUtil.isEmpty(stringList)) {
            return new ArrayList<>();
        }
        return CollectionUtil.toList(stringList, s -> JSONObject.parseObject(s, SysNoticePortalVO.class));
    }


    /**
     * 加载并缓存数据
     *
     * @param consumer 处理方法
     */
    private void loadAndCache(Consumer<List<SysNotice>> consumer) {
        List<SysNotice> list = sysNoticeService.list(
                new LambdaQueryWrapper<SysNotice>()
                        .eq(SysNotice::getShowing, true)
                        .orderByDesc(SysNotice::getTop)
                        .orderByAsc(SysNotice::getSort)
                        .orderByDesc(SysNotice::getUpdateTime));

        Optional.ofNullable(list).ifPresent(consumer);
    }

    /**
     * 解析并缓存数据
     *
     * @param list 公告列表
     */
    private void resolveListForRedis(List<SysNotice> list) {
        List<I18nLanguage> i18nLanguages = i18nLanguageService.list();
        for (I18nLanguage i18nLanguage : i18nLanguages) {
            //清楚原来数据
            redisTemplate.delete(formatCodeKey(i18nLanguage.getCode()));

            List<SysNoticePortalVO> voList = CollectionUtil.toList(list,
                    e -> e.getLanguageCode().equals(i18nLanguage.getCode()),
                    SysNoticePortalVO::new);
            List<String> strings = CollectionUtil.toList(voList, s -> JSONObject.toJSONString(s));
            if (CollectionUtil.isEmpty(strings)) {
                continue;
            }
            listOperations.rightPushAll(formatCodeKey(i18nLanguage.getCode()), strings);

        }
    }

    private String formatCodeKey(String code) {
        return StrUtil.format(RedisKeyEnum.SYS_NOTICE.getVal(), code);
    }


    @Autowired
    public void setListOperations(ListOperations<String, String> listOperations) {
        this.listOperations = listOperations;
    }

    @Autowired
    public void setSysNoticeService(SysNoticeService sysNoticeService) {
        this.sysNoticeService = sysNoticeService;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setI18nLanguageService(I18nLanguageService i18nLanguageService) {
        this.i18nLanguageService = i18nLanguageService;
    }
}
