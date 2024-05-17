package com.im.common.service;

import com.im.common.entity.BankCardRechargeConfigCard;
import com.im.common.param.BankCardRechargeConfigAddParam;
import com.im.common.util.mybatis.service.MyBatisPlusService;

import java.util.Collection;
import java.util.List;

/**
 * 银行卡充值配置-卡列表 服务类
 *
 * @author Barry
 * @date 2021-09-29
 */
public interface BankCardRechargeConfigCardService extends MyBatisPlusService<BankCardRechargeConfigCard> {
    /**
     * 根据配置ID列出可用的
     *
     * @param configId
     * @return
     */
    List<BankCardRechargeConfigCard> listEnabledByConfigId(long configId);

    /**
     * 查询配置银行卡
     *
     * @param configIds
     * @return
     */
    List<BankCardRechargeConfigCard> listByConfigIds(Collection<Long> configIds);

    /**
     * 调整卡
     *
     * @param configId
     * @param configCardParams
     */
    void adjustCards(Long configId, List<BankCardRechargeConfigAddParam.ConfigCardParam> configCardParams);

    /**
     * 根据配置删除卡
     *
     * @param configId
     */
    void deleteByConfigId(long configId);
}
