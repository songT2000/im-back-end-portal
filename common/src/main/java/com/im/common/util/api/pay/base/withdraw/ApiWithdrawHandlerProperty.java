package com.im.common.util.api.pay.base.withdraw;

import com.im.common.entity.enums.ApiWithdrawConfigCodeEnum;

import java.lang.annotation.*;

/**
 * 定义API代付渠道属性
 *
 * @author Barry
 * @date 2020-08-31
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiWithdrawHandlerProperty {
    /**
     * 处理哪个code的，对应api_withdraw_config.code
     *
     * @return
     */
    ApiWithdrawConfigCodeEnum[] value();
}
