package com.im.common.cache.sysconfig;

import com.im.common.entity.enums.SysConfigGroupEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识{@link SysConfigResolver}的子类处理sys_config表中的哪种group数据
 *
 * @author Barry
 * @date 2018/6/8
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SysConfigResolverGroup {
    /**
     * 处理哪个配置项组
     *
     * @return 配置项组
     */
    SysConfigGroupEnum value();
}
