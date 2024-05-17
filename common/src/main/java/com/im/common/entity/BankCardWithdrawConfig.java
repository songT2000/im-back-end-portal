package com.im.common.entity;

import com.im.common.entity.base.BaseDeletableEnableEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 银行卡提现配置
 *
 * @author Barry
 * @date 2021-09-29
 */
@Data
@NoArgsConstructor
public class BankCardWithdrawConfig extends BaseDeletableEnableEntity implements Serializable {
    private static final long serialVersionUID = -476370098334871756L;

    /**
     * 名称
     **/
    private String name;

    /**
     * 金额范围，100~30000为任意值，100,200为选项值，100为最小值且无上限
     **/
    private String amountRange;

    /**
     * 金额最多小数位
     */
    private Integer amountMaxPrecision;

    /**
     * 手续费百分比，0.01就是%1，手续费将从到账金额中扣除
     **/
    private BigDecimal serviceChargePercent;

    /**
     * 排序号，越小排越前面
     */
    private Integer sort;
}
