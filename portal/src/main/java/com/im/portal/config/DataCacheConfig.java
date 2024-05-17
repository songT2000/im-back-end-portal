package com.im.portal.config;

import com.im.common.cache.base.CacheProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

/**
 * 数据缓存配置
 *
 * @author Barry
 * @date 10/10/19
 */
@Component
@ConditionalOnBean(CacheProxy.class)
public class DataCacheConfig {

    @Autowired
    public DataCacheConfig(CacheProxy cacheProxy) {
        // 初始化缓存数据
        cacheProxy.init(false);
    }
}
