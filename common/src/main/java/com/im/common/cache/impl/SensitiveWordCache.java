package com.im.common.cache.impl;

import com.alibaba.fastjson.JSON;
import com.im.common.cache.base.BaseCacheHandler;
import com.im.common.cache.base.CacheProperty;
import com.im.common.constant.RedisKeyEnum;
import com.im.common.entity.SensitiveWord;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.service.SensitiveWordService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.redis.RedisKeySerializer;
import com.im.common.util.redis.RedisValueSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 敏感词
 */
@CacheProperty(value = SysCacheRefreshTypeEnum.SENSITIVE_WORD, redis = true, local = false)
@Component
public class SensitiveWordCache implements BaseCacheHandler {
    private SensitiveWordService sensitiveWordService;
    private HashOperations<String, String, String> redisHash;
    private RedisKeySerializer keySerializer;
    private RedisValueSerializer valueSerializer;

    @Autowired
    public void setSensitiveWordService(SensitiveWordService sensitiveWordService) {
        this.sensitiveWordService = sensitiveWordService;
    }

    @Autowired
    public void setRedisHash(HashOperations<String, String, String> redisHash) {
        this.redisHash = redisHash;
    }

    @Autowired
    public void setKeySerializer(RedisKeySerializer keySerializer) {
        this.keySerializer = keySerializer;
    }

    @Autowired
    public void setValueSerializer(RedisValueSerializer valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    @Override
    public void reloadRedis() {
        loadAndCache(this::resolveListForRedis);
    }

    /**
     * 查询所有列表，包含启用&禁用的
     *
     * @return List<SensitiveWord>
     */
    public List<SensitiveWord> listFromRedis() {
        Map<String, String> entries = redisHash.entries(RedisKeyEnum.SENSITIVE_WORD.getVal());
        if (CollectionUtil.isEmpty(entries)) {
            return new ArrayList<>();
        }

        Collection<String> valuesStrList = entries.values();

        return CollectionUtil.toList(valuesStrList, s -> JSON.parseObject(s, SensitiveWord.class));
    }

    /**
     * 加载并缓存数据
     *
     * @param consumer 处理方法
     */
    private void loadAndCache(Consumer<List<SensitiveWord>> consumer) {
        List<SensitiveWord> list = sensitiveWordService.list();

        consumer.accept(list);
    }

    /**
     * 解析并缓存数据
     *
     * @param list 敏感词列表
     */
    private void resolveListForRedis(List<SensitiveWord> list) {
        // 分组
        Map<Long, SensitiveWord> idMap = CollectionUtil.toMapWithKey(list, e -> e.getId());

        // 组装redis数据
        Map<byte[], byte[]> redisData = idMap
                .entrySet()
                .stream()
                .collect(Collectors
                        .toMap(entry -> keySerializer.serialize(entry.getKey()),
                                entry -> valueSerializer.serialize(entry.getValue())));

        // 设置到redis，使用pipeline减少删增之间的真空时间
        redisHash.getOperations().executePipelined((RedisCallback<Object>) connection -> {
            {
                byte[] key = keySerializer.serialize(RedisKeyEnum.SENSITIVE_WORD.getVal());
                connection.del(key);
                if (CollectionUtil.isNotEmpty(redisData)) {
                    connection.hMSet(key, redisData);
                }
            }

            return null;
        });
    }
}
