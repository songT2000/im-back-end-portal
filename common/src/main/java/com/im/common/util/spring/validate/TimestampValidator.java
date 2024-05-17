package com.im.common.util.spring.validate;

import com.im.common.constant.CommonConstant;
import com.im.common.util.ClockUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 实现{@link TimestampValidate}逻辑，不需要定义为Spring Bean，Spring会自动扫描，可以在类中直接注入Spring类
 * <p>
 * 验证请求时间戳是否过期，对于提供给外部的接口，尽量验证请求有效期是否过去，避免重放攻击
 * <p>
 * 在线时间戳工具：https://tool.lu/timestamp/
 * 我们的要求格式和上面一样
 *
 * @author Barry
 * @date 2019/2/15
 * @see {@link CommonConstant#DEFAULT_REQUEST_TIMESTAMP_TIMEOUT_SECONDS}
 */
public class TimestampValidator implements ConstraintValidator<TimestampValidate, Long> {
    private TimestampValidate annotation;

    @Override
    public void initialize(TimestampValidate constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        Long timestamp = value;

        if (timestamp == null) {
            if (annotation.required()) {
                return false;
            }
        } else {
            // 当前系统时间秒数
            long nowSeconds = ClockUtil.nowMillis() / 1000;

            // timestamp的过期值
            long expireTimestamp = nowSeconds - CommonConstant.DEFAULT_REQUEST_TIMESTAMP_TIMEOUT_SECONDS;

            // 请求时间戳已经过期
            if (timestamp > expireTimestamp) {
                return true;
            }

            return false;
        }
        return true;
    }
}