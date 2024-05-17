package com.im.common.util.api.pay.base.withdraw;

import lombok.Data;

/**
 * 三方请求代付返回
 *
 * @author Barry
 * @date 2021-03-21
 */
@Data
public class ApiWithdrawRequestResponseVO {
    public ApiWithdrawRequestResponseVO(Boolean success, String message) {
        this.success = success;
        this.message = message;
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
     * 成功
     *
     * @return
     */
    public static ApiWithdrawRequestResponseVO success() {
        ApiWithdrawRequestResponseVO rsp = new ApiWithdrawRequestResponseVO(true, null);
        return rsp;
    }

    /**
     * 失败
     *
     * @param message
     * @return
     */
    public static ApiWithdrawRequestResponseVO failed(String message) {
        ApiWithdrawRequestResponseVO rsp = new ApiWithdrawRequestResponseVO(false, message);
        return rsp;
    }
}
