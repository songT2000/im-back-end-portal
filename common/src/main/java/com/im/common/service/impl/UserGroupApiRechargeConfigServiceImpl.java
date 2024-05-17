package com.im.common.service.impl;

import com.im.common.cache.base.CacheProxy;
import com.im.common.cache.impl.ApiRechargeConfigCache;
import com.im.common.cache.impl.UserGroupCache;
import com.im.common.entity.UserGroupApiRechargeConfig;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.mapper.UserGroupApiRechargeConfigMapper;
import com.im.common.param.UserGroupEditRelationIdAdminParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.UserGroupApiRechargeConfigService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 组三方充值配置 服务实现类
 *
 * @author Barry
 * @date 2021-04-11
 */
@Service
public class UserGroupApiRechargeConfigServiceImpl
        extends MyBatisPlusServiceImpl<UserGroupApiRechargeConfigMapper, UserGroupApiRechargeConfig>
        implements UserGroupApiRechargeConfigService {
    private UserGroupCache userGroupCache;
    private CacheProxy cacheProxy;
    private ApiRechargeConfigCache apiRechargeConfigCache;

    @Autowired
    public void setUserGroupCache(UserGroupCache userGroupCache) {
        this.userGroupCache = userGroupCache;
    }

    @Autowired
    public void setCacheProxy(CacheProxy cacheProxy) {
        this.cacheProxy = cacheProxy;
    }

    @Autowired
    public void setApiRechargeConfigCache(ApiRechargeConfigCache apiRechargeConfigCache) {
        this.apiRechargeConfigCache = apiRechargeConfigCache;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse editForAdmin(UserGroupEditRelationIdAdminParam param) {
        // 组必须存在
        String groupName = userGroupCache.getNameByIdFromLocal(param.getGroupId());
        if (StrUtil.isBlank(groupName)) {
            return RestResponse.failed(ResponseCode.USER_GROUP_NOT_FOUND);
        }

        // 判断数据是否存在
        if (CollectionUtil.isNotEmpty(param.getIds())) {
            for (Long apiRechargeConfigId : param.getIds()) {
                String adminName = apiRechargeConfigCache.getAdminNameByIdFromLocal(apiRechargeConfigId);
                if (StrUtil.isBlank(adminName)) {
                    return RestResponse.failed(ResponseCode.API_RECHARGE_CONFIG_NOT_FOUND, apiRechargeConfigId);
                }
            }
        }

        // 删除原来的
        lambdaUpdate().in(UserGroupApiRechargeConfig::getGroupId, param.getGroupId()).remove();

        // 新增本次全部
        if (CollectionUtil.isNotEmpty(param.getIds())) {
            List<UserGroupApiRechargeConfig> addList = CollectionUtil.toList(param.getIds(),
                    e -> new UserGroupApiRechargeConfig(param.getGroupId(), e));
            saveBatch(addList);
        }

        // 刷新缓存
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.USER_GROUP_API_RECHARGE_CONFIG);

        return RestResponse.OK;
    }
}