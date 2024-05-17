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
 * 提现订单
 *
 * @author Barry
 * @date 2021-08-21
 */
@Data
@NoArgsConstructor
public class WithdrawOrder extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -8065120845308857699L;

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
    private WithdrawOrderStatusEnum status;

    /**
     * 提现方式
     */
    private WithdrawOrderTypeEnum type;

    /**
     * 提现配置来源
     */
    private WithdrawConfigSourceEnum requestWithdrawConfigSource;

    /**
     * 提现配置ID
     */
    private Long requestWithdrawConfigId;

    /**
     * 用户卡姓名，提单配置来源是银行卡时才有值
     */
    private String userBankCardName;

    /**
     * 用户银行ID，提单配置来源是银行卡时才有值
     */
    private Long userBankId;

    /**
     * 用户卡号，提单配置来源是银行卡时才有值
     */
    private String userBankCardNum;

    /**
     * 用户卡支行，提单配置来源是银行卡时才有值
     */
    private String userBankCardBranch;

    /**
     * 审核人
     */
    private Long approveAdminId;

    /**
     * 审核时间
     */
    private LocalDateTime approveTime;

    /**
     * 打款人
     */
    private Long payAdminId;

    /**
     * 打款提交时间
     */
    private LocalDateTime payRequestTime;

    /**
     * 打款完成时间
     */
    private LocalDateTime payFinishTime;

    /**
     * 打款方式
     */
    private WithdrawOrderPayTypeEnum payType;

    /**
     * 代付配置ID，只有当使用了API代付时才有值
     */
    private Long apiWithdrawConfigId;

    /**
     * 代付回调IP，只有当使用了API代付且有回调时才有值
     */
    private String apiWithdrawCallbackIp;

    /**
     * 订单完成方式
     */
    private WithdrawOrderFinishTypeEnum finishType;

    /**
     * 请求IP，客人提交的单就是客人的IP，管理员提交的单就是管理员的IP
     */
    private String requestIp;

    /**
     * 备注
     */
    private String remark;

    /**
     * 记账日
     */
    private String reportDate;
}
