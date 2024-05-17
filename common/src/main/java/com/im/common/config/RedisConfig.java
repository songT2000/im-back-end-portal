package com.im.common.config;

import com.im.common.util.redis.RedisConfigUtil;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * redis配置
 *
 * @author Barry
 * @date 2019/2/6
 */
@Configuration
@EnableCaching
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfig extends RedisConfigUtil {
}
