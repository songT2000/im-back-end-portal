package com.im.common.util.api.sms.base;

import lombok.Data;

/**
 * 三方请求充值返回
 *
 * @author Barry
 * @date 2021-03-21
 */
@Data
public class SmsRequestResponseVO {
    public SmsRequestResponseVO(Boolean success, String message) {
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
    public static SmsRequestResponseVO success() {
        SmsRequestResponseVO rsp = new SmsRequestResponseVO(true, null);
        return rsp;
    }

    /**
     * 失败
     *
     * @param message
     * @return
     */
    public static SmsRequestResponseVO failed(String message) {
        SmsRequestResponseVO rsp = new SmsRequestResponseVO(false, message);
        return rsp;
    }
}
