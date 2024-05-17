package com.im.common.util.api.pay.base.recharge;

import lombok.Data;

/**
 * 三方请求充值返回
 *
 * @author Barry
 * @date 2021-03-21
 */
@Data
public class ApiRechargeRequestResponseVO {
    public ApiRechargeRequestResponseVO(Boolean success, String message, RechargeRequestResponseVO response) {
        this.success = success;
        this.message = message;
        this.response = response;
    }

    /**
     * 请求三方是否成功
     */
    private Boolean success;

    /**
     * 请求失败时的消息
     */
    private String message;

    /**
     * 返回数据
     */
    private RechargeRequestResponseVO response;

    /**
     * 成功
     *
     * @param response
     * @return
     */
    public static ApiRechargeRequestResponseVO success(RechargeRequestResponseVO response) {
        ApiRechargeRequestResponseVO rsp = new ApiRechargeRequestResponseVO(true, null, response);
        return rsp;
    }

    /**
     * 失败
     *
     * @param message
     * @return
     */
    public static ApiRechargeRequestResponseVO failed(String message) {
        ApiRechargeRequestResponseVO rsp = new ApiRechargeRequestResponseVO(false, message, null);
        return rsp;
    }
}
