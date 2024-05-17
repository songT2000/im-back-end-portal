package com.im.common.cache.impl;

import com.im.common.cache.base.BaseCacheHandler;
import com.im.common.cache.base.CacheProperty;
import com.im.common.entity.UserGroupBankCardRechargeConfig;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.service.UserGroupBankCardRechargeConfigService;
import com.im.common.util.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * <p>组银行卡充值配置缓存</p>
 *
 * @author Barry
 * @date 2021-04-11
 */
@CacheProperty(value = SysCacheRefreshTypeEnum.USER_GROUP_BANK_CARD_RECHARGE_CONFIG, redis = false, local = true)
@Component
public class UserGroupBankCardRechargeConfigCache implements BaseCacheHandler {
    private UserGroupBankCardRechargeConfigService userGroupBankCardRechargeConfigService;

    /**
     * 每个充值配置的用户组<充值配置ID, [组ID列表]>
     */
    private Map<Long, Set<Long>> LOCAL_BANK_CARD_RECHARGE_CONFIG_ID_GROUP_ID_SET_CACHE = new HashMap<>();

    @Autowired
    public void setUserGroupBankCardRechargeConfigService(UserGroupBankCardRechargeConfigService userGroupBankCardRechargeConfigService) {
        this.userGroupBankCardRechargeConfigService = userGroupBankCardRechargeConfigService;
    }

    @Override
    public void reloadLocal() {
        loadAndCache();
    }

    public Set<Long> getGroupSetByBankCardRechargeConfigIdFromLocal(Long bankCardRechargeConfigId) {
        if (bankCardRechargeConfigId == null) {
            return new TreeSet<>();
        }
        return Optional.ofNullable(LOCAL_BANK_CARD_RECHARGE_CONFIG_ID_GROUP_ID_SET_CACHE.get(bankCardRechargeConfigId)).orElse(new TreeSet<>());
    }

    /**
     * 加载并缓存数据
     */
    private void loadAndCache() {
        List<UserGroupBankCardRechargeConfig> userGroupBankCardRechargeConfigList = userGroupBankCardRechargeConfigService.list();
        if (CollectionUtil.isEmpty(userGroupBankCardRechargeConfigList)) {
            this.LOCAL_BANK_CARD_RECHARGE_CONFIG_ID_GROUP_ID_SET_CACHE = new HashMap<>();
            return;
        }

        Map<Long, Set<Long>> bankCardRechargeConfigIdGroupIdSetTmp = new HashMap<>();

        for (UserGroupBankCardRechargeConfig config : userGroupBankCardRechargeConfigList) {
            Long bankCardRechargeConfigId = config.getBankCardRechargeConfigId();
            Long groupId = config.getGroupId();

            bankCardRechargeConfigIdGroupIdSetTmp.compute(bankCardRechargeConfigId, (key, value) -> {
                value = Optional.ofNullable(value).orElse(new TreeSet<>());
                value.add(groupId);
                return value;
            });
        }

        this.LOCAL_BANK_CARD_RECHARGE_CONFIG_ID_GROUP_ID_SET_CACHE = bankCardRechargeConfigIdGroupIdSetTmp;
    }
}
