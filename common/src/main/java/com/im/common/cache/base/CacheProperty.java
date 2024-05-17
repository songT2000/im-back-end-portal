package com.im.common.cache.base;

import com.im.common.entity.enums.SysCacheRefreshTypeEnum;

import java.lang.annotation.*;

/**
 * 定义缓存属性
 *
 * @author Barry
 * @date 2018/6/8
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheProperty {
    /**
     * 处理哪个缓存类型
     *
     * @return 缓存类型
     */
    SysCacheRefreshTypeEnum value();

    /**
     * 是否有redis缓存
     *
     * @return boolean
     */
    boolean redis();

    /**
     * 是否有local缓存
     *
     * @return boolean
     */
    boolean local();
}
