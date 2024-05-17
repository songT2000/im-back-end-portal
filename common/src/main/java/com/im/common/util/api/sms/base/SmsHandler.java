package com.im.common.util.api.sms.base;

import com.im.common.entity.SmsChannelConfig;

/**
 * 短信信令通道处理基类
 *
 * @author Barry
 * @date 2022-02-10
 */
public interface SmsHandler {
    /**
     * 发送短信
     *
     * @param channel      配置
     * @param mobilePrefix 国际区号
     * @param mobile       手机号，不要带有国际区号，比如发给国内，就13888888888，发给菲律宾9560111111
     * @param message      短信文本
     * @return
     * @throws Exception
     */
    SmsRequestResponseVO send(SmsChannelConfig channel, String mobilePrefix, String mobile, String message) throws Exception;
}
