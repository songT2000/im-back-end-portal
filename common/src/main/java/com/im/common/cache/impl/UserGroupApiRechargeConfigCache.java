package com.im.common.cache.impl;

import com.im.common.cache.base.BaseCacheHandler;
import com.im.common.cache.base.CacheProperty;
import com.im.common.entity.UserGroupApiRechargeConfig;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.service.UserGroupApiRechargeConfigService;
import com.im.common.util.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * <p>组三方充值配置缓存</p>
 *
 * @author Barry
 * @date 2021-04-11
 */
@CacheProperty(value = SysCacheRefreshTypeEnum.USER_GROUP_API_RECHARGE_CONFIG, redis = false, local = true)
@Component
public class UserGroupApiRechargeConfigCache implements BaseCacheHandler {
    private UserGroupApiRechargeConfigService userGroupApiRechargeConfigService;

    /**
     * 每个充值配置的用户组<充值配置ID, [组ID列表]>
     */
    private Map<Long, Set<Long>> LOCAL_API_RECHARGE_CONFIG_ID_GROUP_ID_SET_CACHE = new HashMap<>();

    @Autowired
    public void setUserGroupApiRechargeConfigService(UserGroupApiRechargeConfigService userGroupApiRechargeConfigService) {
        this.userGroupApiRechargeConfigService = userGroupApiRechargeConfigService;
    }

    @Override
    public void reloadLocal() {
        loadAndCache();
    }

    public Set<Long> getGroupSetByApiRechargeConfigIdFromLocal(Long apiRechargeConfigId) {
        if (apiRechargeConfigId == null) {
            return new TreeSet<>();
        }
        return Optional.ofNullable(LOCAL_API_RECHARGE_CONFIG_ID_GROUP_ID_SET_CACHE.get(apiRechargeConfigId)).orElse(new TreeSet<>());
    }

    /**
     * 加载并缓存数据
     */
    private void loadAndCache() {
        List<UserGroupApiRechargeConfig> userGroupApiRechargeConfigList = userGroupApiRechargeConfigService.list();
        if (CollectionUtil.isEmpty(userGroupApiRechargeConfigList)) {
            this.LOCAL_API_RECHARGE_CONFIG_ID_GROUP_ID_SET_CACHE = new HashMap<>();
            return;
        }

        Map<Long, Set<Long>> apiRechargeConfigIdGroupIdSetTmp = new HashMap<>();

        for (UserGroupApiRechargeConfig config : userGroupApiRechargeConfigList) {
            Long apiRechargeConfigId = config.getApiRechargeConfigId();
            Long groupId = config.getGroupId();

            apiRechargeConfigIdGroupIdSetTmp.compute(apiRechargeConfigId, (key, value) -> {
                value = Optional.ofNullable(value).orElse(new TreeSet<>());
                value.add(groupId);
                return value;
            });
        }

        this.LOCAL_API_RECHARGE_CONFIG_ID_GROUP_ID_SET_CACHE = apiRechargeConfigIdGroupIdSetTmp;
    }
}
