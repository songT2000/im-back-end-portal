package com.im.common.service;

import com.im.common.response.RestResponse;

/**
 * 短信相关
 *
 * @author Barry
 * @date 2022-02-10
 */
public interface SmsService {
    /**
     * 发送验证码，6位数字
     *
     * @param countryCode
     * @param mobile
     * @param ip          同一IP，最多同时存在5笔验证码
     * @return
     */
    RestResponse sendVerificationCode(String countryCode, String mobile, String ip);

    /**
     * 验证验证码
     *
     * @param countryCode
     * @param mobile
     * @param code   填写的验证码
     * @return
     */
    RestResponse verifyVerificationCode(String countryCode, String mobile, String code);
}
