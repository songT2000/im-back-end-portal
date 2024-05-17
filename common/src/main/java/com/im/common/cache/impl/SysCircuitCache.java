package com.im.common.cache.impl;

import com.alibaba.fastjson.JSON;
import com.im.common.cache.base.BaseCacheHandler;
import com.im.common.cache.base.CacheProperty;
import com.im.common.constant.RedisKeyEnum;
import com.im.common.entity.SysCircuit;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.service.SysCircuitService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.redis.RedisKeySerializer;
import com.im.common.util.redis.RedisValueSerializer;
import com.im.common.vo.SysCircuitPortalVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 公告缓存
 *
 * @author max.stark
 * @date 2021-01-26
 */
@CacheProperty(value = SysCacheRefreshTypeEnum.SYS_CIRCUIT, redis = true, local = false)
@Component
public class SysCircuitCache implements BaseCacheHandler {
    private SysCircuitService sysCircuitService;
    private ListOperations<String, String> listOperations;
    private RedisKeySerializer keySerializer;
    private RedisValueSerializer valueSerializer;

    @Autowired
    public void setSysCircuitService(SysCircuitService sysCircuitService) {
        this.sysCircuitService = sysCircuitService;
    }

    @Autowired
    public void setListOperations(ListOperations<String, String> listOperations) {
        this.listOperations = listOperations;
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
     * 加载并缓存数据
     *
     * @param consumer 处理方法
     */
    private void loadAndCache(Consumer<List<SysCircuit>> consumer) {
        List<SysCircuit> list = sysCircuitService.lambdaQuery()
                .eq(SysCircuit::getEnabled, true)
                .orderByAsc(SysCircuit::getCreateTime)
                .list();

        Optional.ofNullable(list).ifPresent(consumer);
    }

    /**
     * 解析并缓存数据
     *
     * @param list 公告列表
     */
    private void resolveListForRedis(List<SysCircuit> list) {
        List<SysCircuitPortalVO> voList = CollectionUtil.toList(list, SysCircuitPortalVO::new);

        // redis缓存
        {
            // 组装redis数据
            byte[][] redisData = new byte[voList.size()][];
            int i = 0;
            for (SysCircuitPortalVO vo : voList) {
                redisData[i++] = valueSerializer.serialize(JSON.toJSONString(vo));
            }

            // 设置到redis，使用pipeline减少删增之间的真空时间
            listOperations.getOperations().executePipelined((RedisCallback<Object>) connection -> {
                {
                    byte[] key = keySerializer.serialize(RedisKeyEnum.SYS_CIRCUIT.getVal());
                    connection.del(key);
                    if (redisData != null && redisData.length > 0) {
                        connection.rPush(key, redisData);
                    }
                }
                return null;
            });
        }
    }
}
