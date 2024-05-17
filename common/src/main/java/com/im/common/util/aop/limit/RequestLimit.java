package com.im.common.util.aop.limit;

import com.im.common.util.StrUtil;

import java.lang.annotation.*;

/**
 * 分布式限速注解
 *
 * @author Barry
 * @date 2020-03-21
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RequestLimit {
    /**
     * 限速规则
     *
     * @return RequestLimitTypeEnum
     */

    RequestLimitTypeEnum type() default RequestLimitTypeEnum.USER;

    /**
     * 在多少second内最多允许调用count次，小于等于0则不生效
     * <p>
     * 默认是60秒内最多允许调用100次
     *
     * @return second
     */
    int second() default 60;

    /**
     * 调用的次数，小于等于0则不生效
     *
     * @return count
     */
    int count() default 60;

    /**
     * 当类型为REQUEST_KEY时，该值必填
     *
     * @return count
     */
    String requestBodyKey() default StrUtil.EMPTY;
}