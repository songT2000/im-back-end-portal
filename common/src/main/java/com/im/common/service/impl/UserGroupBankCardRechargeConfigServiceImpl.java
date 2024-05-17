package com.im.common.service.impl;

import com.im.common.cache.base.CacheProxy;
import com.im.common.cache.impl.BankCardRechargeConfigCache;
import com.im.common.cache.impl.UserGroupCache;
import com.im.common.entity.UserGroupBankCardRechargeConfig;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.mapper.UserGroupBankCardRechargeConfigMapper;
import com.im.common.param.UserGroupEditRelationIdAdminParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.UserGroupBankCardRechargeConfigService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 组银行卡充值配置 服务实现类
 *
 * @author Barry
 * @date 2021-04-11
 */
@Service
public class UserGroupBankCardRechargeConfigServiceImpl
        extends MyBatisPlusServiceImpl<UserGroupBankCardRechargeConfigMapper, UserGroupBankCardRechargeConfig>
        implements UserGroupBankCardRechargeConfigService {
    private UserGroupCache userGroupCache;
    private CacheProxy cacheProxy;
    private BankCardRechargeConfigCache bankCardRechargeConfigCache;

    @Autowired
    public void setUserGroupCache(UserGroupCache userGroupCache) {
        this.userGroupCache = userGroupCache;
    }

    @Autowired
    public void setCacheProxy(CacheProxy cacheProxy) {
        this.cacheProxy = cacheProxy;
    }

    @Autowired
    public void setBankCardRechargeConfigCache(BankCardRechargeConfigCache bankCardRechargeConfigCache) {
        this.bankCardRechargeConfigCache = bankCardRechargeConfigCache;
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
            for (Long bankCardRechargeConfigId : param.getIds()) {
                String name = bankCardRechargeConfigCache.getNameByIdFromLocal(bankCardRechargeConfigId);
                if (StrUtil.isBlank(name)) {
                    return RestResponse.failed(ResponseCode.BANK_CARD_RECHARGE_CONFIG_NOT_FOUND, bankCardRechargeConfigId);
                }
            }
        }

        // 删除原来的
        lambdaUpdate().in(UserGroupBankCardRechargeConfig::getGroupId, param.getGroupId()).remove();

        // 新增本次全部
        if (CollectionUtil.isNotEmpty(param.getIds())) {
            List<UserGroupBankCardRechargeConfig> addList = CollectionUtil.toList(param.getIds(),
                    e -> new UserGroupBankCardRechargeConfig(param.getGroupId(), e));
            saveBatch(addList);
        }

        // 刷新缓存
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.USER_GROUP_BANK_CARD_RECHARGE_CONFIG);

        return RestResponse.OK;
    }
}