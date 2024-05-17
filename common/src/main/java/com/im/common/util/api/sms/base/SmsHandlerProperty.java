package com.im.common.util.api.sms.base;

import com.im.common.entity.enums.SmsChannelTypeEnum;

import java.lang.annotation.*;

/**
 * 定义属性
 *
 * @author Barry
 * @date 2020-08-31
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SmsHandlerProperty {
    /**
     * 处理哪个type的，对应sms_channel_config.type
     *
     * @return
     */
    SmsChannelTypeEnum[] value();
}
