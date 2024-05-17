package com.im.common.util.api.pay.base.withdraw;

import com.im.common.entity.enums.WithdrawOrderStatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 代付订单回调/查询验证结果
 *
 * @author Barry
 * @date 2020-08-30
 */
@Data
@NoArgsConstructor
public class ApiWithdrawVerifyResult {
    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 状态
     */
    private WithdrawOrderStatusEnum status;

    /**
     * 失败原因，记录到系统日志里的
     */
    private String message;

    /**
     * 输出给第三方的
     */
    private String output;

    public ApiWithdrawVerifyResult(Long orderId, WithdrawOrderStatusEnum status, String message, String output) {
        this.orderId = orderId;
        this.status = status;
        this.message = message;
        this.output = output;
    }

    public static ApiWithdrawVerifyResult paying(Long orderId) {
        return new ApiWithdrawVerifyResult(orderId, WithdrawOrderStatusEnum.PAYING, null, null);
    }

    public static ApiWithdrawVerifyResult paySuccess(Long orderId, String output) {
        return new ApiWithdrawVerifyResult(orderId, WithdrawOrderStatusEnum.PAY_SUCCESS, null, output);
    }

    public static ApiWithdrawVerifyResult payFailed(Long orderId, String message, String output) {
        return new ApiWithdrawVerifyResult(orderId, WithdrawOrderStatusEnum.PAY_FAILED, message, output);
    }
}
