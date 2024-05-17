package com.im.common.util.ip;

import cn.hutool.core.util.StrUtil;

import java.lang.annotation.*;

/**
 * 自动在spring中将本次用户访问的http IP注入到参数中
 *
 * @author Barry
 * @date 2019/2/14
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestIp {
    /**
     * 是否强制需要
     *
     * @return
     */
    boolean required() default true;

    /**
     * 错误消息
     */
    String message() default StrUtil.EMPTY;
}
