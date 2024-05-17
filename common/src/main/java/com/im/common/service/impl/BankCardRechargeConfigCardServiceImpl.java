package com.im.common.service.impl;

import com.im.common.entity.BankCardRechargeConfigCard;
import com.im.common.mapper.BankCardRechargeConfigCardMapper;
import com.im.common.param.BankCardRechargeConfigAddParam;
import com.im.common.service.BankCardRechargeConfigCardService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 银行卡充值配置-卡列表 服务实现类
 *
 * @author Barry
 * @date 2021-09-29
 */
@Service
public class BankCardRechargeConfigCardServiceImpl
        extends MyBatisPlusServiceImpl<BankCardRechargeConfigCardMapper, BankCardRechargeConfigCard>
        implements BankCardRechargeConfigCardService {

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<BankCardRechargeConfigCard> listEnabledByConfigId(long configId) {
        return lambdaQuery()
                .eq(BankCardRechargeConfigCard::getConfigId, configId)
                .eq(BankCardRechargeConfigCard::getEnabled, true)
                .list();
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<BankCardRechargeConfigCard> listByConfigIds(Collection<Long> configIds) {
        if (CollectionUtil.isEmpty(configIds)) {
            return new ArrayList<>();
        }
        return lambdaQuery().in(BankCardRechargeConfigCard::getConfigId, configIds).list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adjustCards(Long configId, List<BankCardRechargeConfigAddParam.ConfigCardParam> configCardParams) {
        if (CollectionUtil.isEmpty(configCardParams)) {
            return;
        }
        // 先拿出原来的数据
        List<BankCardRechargeConfigCard> oldList = lambdaQuery().eq(BankCardRechargeConfigCard::getConfigId, configId).list();
        Set<Long> oldIdSet = CollectionUtil.toSet(oldList, e -> e.getId());

        // 新增，没有ID的都是新增
        if (CollectionUtil.isNotEmpty(configCardParams)) {
            List<BankCardRechargeConfigAddParam.ConfigCardParam> addParamList
                    = CollectionUtil.filterList(configCardParams, param -> param.getId() == null);
            if (CollectionUtil.isNotEmpty(addParamList)) {
                List<BankCardRechargeConfigCard> addList = CollectionUtil.toList(addParamList, e -> new BankCardRechargeConfigCard(configId, e));
                saveBatch(addList);
            }
        }

        // 删除，参数没有，原来有，那就是删除的
        if (CollectionUtil.isNotEmpty(oldList)) {
            Set<Long> paramIdSet = CollectionUtil.toSet(configCardParams, e -> e.getId() != null, e -> e.getId());

            if (CollectionUtil.isEmpty(paramIdSet)) {
                // 参数是空的，那全部删除
                lambdaUpdate().eq(BankCardRechargeConfigCard::getConfigId, configId).remove();
            } else {
                List<Long> deleteIdList = CollectionUtil.filterList(oldIdSet, e -> !paramIdSet.contains(e));
                if (CollectionUtil.isNotEmpty(deleteIdList)) {
                    removeByIds(deleteIdList);
                }
            }
        }

        // 编辑，参数有，原来有，那就是编辑的
        if (CollectionUtil.isNotEmpty(configCardParams)) {

            for (BankCardRechargeConfigAddParam.ConfigCardParam param : configCardParams) {
                if (param.getId() == null) {
                    continue;
                }
                BankCardRechargeConfigCard old = CollectionUtil.findFirst(oldList, e -> e.getId().equals(param.getId()));
                if (old != null) {
                    lambdaUpdate()
                            .eq(BankCardRechargeConfigCard::getId, old.getId())
                            .set(BankCardRechargeConfigCard::getCardNum, StrUtil.cleanBlank(param.getCardNum()))
                            .set(BankCardRechargeConfigCard::getBankId, param.getBankId())
                            .set(BankCardRechargeConfigCard::getCardName, StrUtil.trim(param.getCardName()))
                            .set(BankCardRechargeConfigCard::getCardBranch, StrUtil.trim(param.getCardBranch()))
                            .update();
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByConfigId(long configId) {
        lambdaUpdate().eq(BankCardRechargeConfigCard::getConfigId, configId).remove();
    }
}
