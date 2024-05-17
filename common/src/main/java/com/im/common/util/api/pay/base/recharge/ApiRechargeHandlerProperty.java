package com.im.common.util.api.pay.base.recharge;

import com.im.common.entity.enums.ApiRechargeConfigCodeEnum;

import java.lang.annotation.*;

/**
 * 定义三方充值渠道属性
 *
 * @author Barry
 * @date 2020-08-31
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiRechargeHandlerProperty {
    /**
     * 处理哪个code的，对应api_recharge_config.code
     *
     * @return
     */
    ApiRechargeConfigCodeEnum[] value();
}
