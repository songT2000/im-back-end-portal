package com.im.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.im.common.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 组银行卡充值配置，关联到组的数据只能组内可见
 *
 * @author Barry
 * @date 2022-03-12
 */
@Data
@NoArgsConstructor
public class UserGroupBankCardRechargeConfig extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 440394066511510972L;

    public UserGroupBankCardRechargeConfig(Long groupId, Long bankCardRechargeConfigId) {
        this.groupId = groupId;
        this.bankCardRechargeConfigId = bankCardRechargeConfigId;
    }

    public UserGroupBankCardRechargeConfig(Long bankCardRechargeConfigId) {
        this.bankCardRechargeConfigId = bankCardRechargeConfigId;
    }

    /**
     * 分布式ID
     **/
    @TableId
    private Long id;

    /**
     * 组ID
     **/
    private Long groupId;

    /**
     * 银行卡充值配置ID
     **/
    private Long bankCardRechargeConfigId;
}
