package com.im.common.util.spring.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 验证Spring参数中的timestamp字段是否合法
 * <p>
 * 把该注解放在需要验证的字段上，或继承{@link TimestampRequestParam}类
 *
 * @author Barry
 * @date 2019/2/14
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = TimestampValidator.class)
public @interface TimestampValidate {
    /**
     * 是否强制需要，抛出{@link IllegalArgumentException}异常，一般默认false就可，用另外的NotNull去限制
     *
     * @return
     */
    boolean required() default false;

    /**
     * 验证不通过时的消息
     *
     * @return
     */
    String message() default "filed timestamp is illegal";

    /**
     * 组
     *
     * @return
     */
    Class<?>[] groups() default {};

    /**
     * payload
     *
     * @return
     */
    Class<? extends Payload>[] payload() default {};
}
