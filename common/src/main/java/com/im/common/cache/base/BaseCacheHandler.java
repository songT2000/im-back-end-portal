package com.im.common.cache.base;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import org.springframework.beans.factory.InitializingBean;

import java.time.Duration;
import java.time.Instant;

/**
 * 缓存接口规范,接口实现类尽量只关注自己的逻辑,缓存刷新及初始化等由{@link CacheProxy}完成
 * <p>
 * 如果需要定时刷新数据,请自行在类中定义
 * <p>
 * 增加缓存步骤
 * 1: 增加{@link com.im.common.entity.enums.SysCacheRefreshTypeEnum}类型
 * 2: 在本目录impl目录下实现本接口，且配置{@link CacheProperty}注解在类上
 * 3: 在需要该缓存的工程DataCacheConfig类中声明成spring对象
 * 4: 在scheduler工程DataCacheConfig类中声明成spring对象
 *
 * @author Barry
 * @date 2018/6/8
 */
public interface BaseCacheHandler extends InitializingBean {
    /**
     * 初始化缓存
     *
     * @throws Exception
     */
    @Override
    default void afterPropertiesSet() throws Exception {
        init();
    }

    /**
     * 初始化缓存
     */
    default void init() {
        Instant start = beforeInit();

        reloadRedis();
        reloadLocal();

        afterInit(start);
    }

    /**
     * 执行init前执行
     *
     * @return 当前时间
     */
    default Instant beforeInit() {
        // 子类必须在类上加入CacheProperty
        CacheProperty cacheProperty = getClass().getAnnotation(CacheProperty.class);
        SysCacheRefreshTypeEnum typeEnum = cacheProperty.value();
        Log log = LogFactory.get(getClass());
        log.info("正在初始化[{}]缓存", typeEnum);

        CacheFactory.putHandler(typeEnum, this);

        return Instant.now();
    }

    /**
     * 执行init后执行
     *
     * @param start beforeInit返回的数据
     */
    default void afterInit(Instant start) {
        CacheProperty cacheProperty = getClass().getAnnotation(CacheProperty.class);
        SysCacheRefreshTypeEnum typeEnum = cacheProperty.value();
        Log log = LogFactory.get(getClass());

        long spentMills = Duration.between(start, Instant.now()).toMillis();

        log.info("初始化[{}]缓存完成，耗时{}ms", typeEnum, spentMills);
    }

    /**
     * 刷新redis缓存,子类如没有该逻辑可不实现该方法
     */
    default void reloadRedis() {
    }

    /**
     * 刷新本地缓存,子类如没有该逻辑可不实现该方法
     */
    default void reloadLocal() {
    }
}
