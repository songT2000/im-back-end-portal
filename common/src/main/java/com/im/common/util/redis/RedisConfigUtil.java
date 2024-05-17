package com.im.common.util.redis;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.*;

/**
 * redis配置
 * <p>
 * 使用方法如下：
 * 1：工程继承此类，编写RedisConfig类，并在类上声明如下两个注解
 *
 * @author Barry
 * @Configuration
 * @AutoConfigureAfter(RedisAutoConfiguration.class) 2：按照Spring的方式，配置好redis连接信息
 * @date 2019/2/6
 */
public class RedisConfigUtil extends CachingConfigurerSupport {

    /**
     * 自定义缓存key的生成策略。
     * 默认的生成策略是看不懂的(乱码内容) 通过Spring 的依赖注入特性进行自定义的配置注入并且此类是一个配置类可以更多程度的自定义配置
     *
     * @return KeyGenerator
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }

    /**
     * 选择redis作为默认缓存工具
     *
     * @param factory
     * @return
     */
    @Bean
    public CacheManager cacheManager(LettuceConnectionFactory factory) {
        // 以锁写入的方式创建RedisCacheWriter对象
        RedisCacheWriter writer = RedisCacheWriter.lockingRedisCacheWriter(factory);

        // 创建默认缓存配置对象
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        RedisCacheManager cacheManager = new RedisCacheManager(writer, config);
        return cacheManager;
    }

    @Bean
    public RedisKeySerializer keySerializer() {
        return new RedisKeySerializer();
    }

    @Bean
    public RedisValueSerializer valueSerializer() {
        return new RedisValueSerializer();
    }

    /**
     * 设置数据存入redis 的序列化方式
     * redisTemplate序列化默认使用的jdkSerializeable
     * 存储二进制字节码，导致key会出现乱码，所以自定义序列化类
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(LettuceConnectionFactory factory,
                                                       RedisKeySerializer keySerializer,
                                                       RedisValueSerializer valueSerializer) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        // 配置连接工厂
        redisTemplate.setConnectionFactory(factory);

        // 使用FastJson来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setHashKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setDefaultSerializer(keySerializer);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    /**
     * 对hash类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public HashOperations<String, String, String> hashOperations(RedisTemplate<String, String> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    /**
     * 对redis字符串类型数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ValueOperations<String, String> valueOperations(RedisTemplate<String, String> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    /**
     * 对链表类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ListOperations<String, String> listOperations(RedisTemplate<String, String> redisTemplate) {
        return redisTemplate.opsForList();
    }

    /**
     * 对无序集合类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public SetOperations<String, String> setOperations(RedisTemplate<String, String> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    /**
     * 对有序集合类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ZSetOperations<String, String> zSetOperations(RedisTemplate<String, String> redisTemplate) {
        return redisTemplate.opsForZSet();
    }
}
