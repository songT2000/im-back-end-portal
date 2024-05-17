package com.im.common.util.url;

import cn.hutool.core.util.StrUtil;

import java.lang.annotation.*;

/**
 * 自动在spring中将本次用户访问的域名注入到参数中
 *
 * @author Barry
 * @date 2019/2/14
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestWebsite {
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
