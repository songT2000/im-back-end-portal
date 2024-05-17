package com.im.common.entity;

import com.im.common.entity.base.BaseDeletableEnableEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 银行卡充值配置（公司入款）
 *
 * @author Barry
 * @date 2021-09-29
 */
@Data
@NoArgsConstructor
public class BankCardRechargeConfig extends BaseDeletableEnableEntity implements Serializable {
    private static final long serialVersionUID = 61116160411734387L;

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
     * 手续费百分比，0.01就是%1
     **/
    private BigDecimal serviceChargePercent;

    /**
     * 启用时间段，格式HH:mm:ss~HH:mm:ss，为空或闭环为全天可用，09:00:00~00:00:00，格式错误全天不可用，为空不生效
     */
    private String enableTime;

    /**
     * 是否需要输入付款人，针对像卡转卡这种
     */
    private Boolean needInputUserCardName;

    /**
     * 排序号，越小排越前面
     */
    private Integer sort;
}
