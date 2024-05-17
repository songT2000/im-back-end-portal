package com.im.common.util.spring.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 验证Spring参数中的IP字段是否合法
 *
 * @author Barry
 * @date 2019/2/14
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = IpValidator.class)
public @interface IpValidate {
    /**
     * 是否强制需要，如果为true且没有IP，则抛出{@link IllegalArgumentException}异常
     *
     * @return
     */
    boolean required() default true;

    /**
     * 验证不通过时的消息
     *
     * @return
     */
    String message() default "Can't recognize ip address";

    /**
     * 是否支持多个，多个以英文逗号分割
     *
     * @return
     */
    boolean multiple() default true;

    /**
     * 是否支持IPV4，127.0.0.0
     *
     * @return
     */
    boolean supportIpv4() default true;

    /**
     * 是否支持IPV4掩码，127.0.0.0/16
     *
     * @return
     */
    boolean supportIpv4Mask() default false;

    /**
     * 是否支持IPV4段，127.0.0.0~127.0.0.255
     *
     * @return
     */
    boolean supportIpv4Range() default false;

    /**
     * 是否支持IPV6
     *
     * @return
     */
    boolean supportIpv6() default true;

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
