package com.im.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.im.common.entity.base.BaseEntity;
import com.im.common.entity.enums.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 充值订单
 *
 * @author Barry
 * @date 2021-08-21
 */
@Data
@NoArgsConstructor
public class RechargeOrder extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 6660583413800414338L;

    /**
     * 分布式ID
     **/
    @TableId
    private Long id;

    /**
     * 订单号
     */
    private String orderNum;

    /**
     * 用户ID
     **/
    private Long userId;


    /**
     * 提单金额
     */
    private BigDecimal requestAmount;

    /**
     * 实际支付金额
     */
    private BigDecimal payAmount;

    /**
     * 实际到账，到账到用户账户里，扣除了手续费的
     */
    private BigDecimal receiveAmount;

    /**
     * 手续费比例
     */
    private BigDecimal serviceChargePercent;

    /**
     * 手续费，从实际到账里扣
     */
    private BigDecimal serviceCharge;

    /**
     * 状态
     */
    private RechargeOrderStatusEnum status;

    /**
     * 充值方式
     */
    private RechargeOrderTypeEnum type;

    /**
     * 配置来源，只可能会是银行卡充值，三方充值
     */
    private RechargeConfigSourceEnum rechargeConfigSource;

    /**
     * 充值配置ID
     */
    private Long rechargeConfigId;

    /**
     * 用户输入的付款人，充值配置要求时才会有值
     */
    private String userCardName;

    /**
     * 收款银行ID，充值配置有返回才有值
     */
    private Long receiveBankId;

    /**
     * 收款银行名，这个字段跟银行ID冗余，比如用三方的收款卡时，就用这个名字，用自己的收款卡时，就用银行ID，充值配置有返回才有值
     */
    private String receiveBankName;

    /**
     * 收款卡姓名，充值配置有返回才有值
     */
    private String receiveBankCardName;

    /**
     * 收款卡号，充值配置有返回才有值
     */
    private String receiveBankCardNum;

    /**
     * 收款卡支行，充值配置有返回才有值
     */
    private String receiveBankCardBranch;

    /**
     * 请求IP，客人提交的单就是客人的IP，管理员提交的单就是管理员的IP
     */
    private String requestIp;

    /**
     * 备注，仅管理员可见
     */
    private String remark;

    /**
     * 三方充值回调IP，充值配置是三方且有回调时才有值
     */
    private String apiRechargeCallbackIp;

    /**
     * 订单完成方式，管理员补单/三方回调/系统查询
     **/
    private RechargeOrderFinishTypeEnum finishType;

    /**
     * 订单完成时间
     **/
    private LocalDateTime finishTime;

    /**
     * 记账日
     */
    private String reportDate;
}
