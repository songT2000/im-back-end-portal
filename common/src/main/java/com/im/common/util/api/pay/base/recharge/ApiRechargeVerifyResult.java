package com.im.common.util.api.pay.base.recharge;

import com.im.common.entity.enums.RechargeOrderStatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 充值订单回调/查询验证结果
 *
 * @author Barry
 * @date 2020-08-30
 */
@Data
@NoArgsConstructor
public class ApiRechargeVerifyResult {
    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 状态
     */
    private RechargeOrderStatusEnum status;

    /**
     * 付款人，如果不为空，则会修改订单的该字段
     **/
    private String userCardName;

    /**
     * 实际支付金额
     */
    private BigDecimal payAmount;

    /**
     * 失败原因，记录到系统日志里的
     */
    private String message;

    /**
     * 输出给第三方的
     */
    private String output;

    public ApiRechargeVerifyResult(Long orderId, RechargeOrderStatusEnum status, String userCardName, BigDecimal payAmount, String message, String output) {
        this.orderId = orderId;
        this.status = status;
        this.userCardName = userCardName;
        this.payAmount = payAmount;
        this.message = message;
        this.output = output;
    }

    public static ApiRechargeVerifyResult waiting(Long orderId) {
        return new ApiRechargeVerifyResult(orderId, RechargeOrderStatusEnum.WAITING, null, null, null, null);
    }

    public static ApiRechargeVerifyResult finished(Long orderId, String userCardName, BigDecimal payAmount, String output) {
        return new ApiRechargeVerifyResult(orderId, RechargeOrderStatusEnum.FINISHED, userCardName, payAmount, output, null);
    }

    public static ApiRechargeVerifyResult failed(Long orderId, String message, String output) {
        return new ApiRechargeVerifyResult(orderId, RechargeOrderStatusEnum.FAILED, null, null, message, output);
    }
}
