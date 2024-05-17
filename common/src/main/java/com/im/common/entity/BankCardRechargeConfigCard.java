package com.im.common.entity;

import cn.hutool.core.util.StrUtil;
import com.im.common.entity.base.BaseEnableEntity;
import com.im.common.param.BankCardRechargeConfigAddParam;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 银行卡充值配置-卡列表
 *
 * @author Barry
 * @date 2021-09-29
 */
@Data
@NoArgsConstructor
public class BankCardRechargeConfigCard extends BaseEnableEntity implements Serializable {
    private static final long serialVersionUID = 4091471286120824011L;

    public BankCardRechargeConfigCard (Long configId, BankCardRechargeConfigAddParam.ConfigCardParam param){
        this.configId = configId;
        this.bankId = param.getBankId();
        this.cardName = StrUtil.trim(param.getCardName());
        this.cardNum = StrUtil.cleanBlank(param.getCardNum());
        this.cardBranch = StrUtil.trim(param.getCardBranch());
        this.enabled = param.getEnabled();
    }

    /**
     * 充值配置ID
     **/
    private Long configId;

    /**
     * 银行ID
     **/
    private Long bankId;

    /**
     * 卡姓名
     **/
    private String cardName;

    /**
     * 卡号
     **/
    private String cardNum;

    /**
     * 支行
     **/
    private String cardBranch;
}
