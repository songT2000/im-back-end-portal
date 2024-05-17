package com.im.common.cache.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.cache.base.BaseCacheHandler;
import com.im.common.cache.base.CacheProperty;
import com.im.common.constant.RedisKeyEnum;
import com.im.common.entity.Bank;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.service.BankService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.redis.RedisKeySerializer;
import com.im.common.util.redis.RedisValueSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 银行缓存
 *
 * @author Barry
 * @date 2019/10/25
 */
@CacheProperty(value = SysCacheRefreshTypeEnum.BANK, redis = true, local = true)
@Component
public class BankCache implements BaseCacheHandler {
    private BankService bankService;
    private HashOperations<String, String, String> redisHash;
    private RedisKeySerializer keySerializer;
    private RedisValueSerializer valueSerializer;

    /**
     * 本地缓存
     */
    private Map<Long, String> LOCAL_ID_NAME_CACHE = new HashMap<>();
    private Map<Long, String> LOCAL_ID_CODE_CACHE = new HashMap<>();
    private Map<String, Long> LOCAL_CODE_ID_CACHE = new HashMap<>();
    private Map<String, String> LOCAL_CODE_NAME_CACHE = new HashMap<>();

    @Autowired
    public void setBankService(BankService bankService) {
        this.bankService = bankService;
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

    @Override
    public void reloadLocal() {
        loadAndCache(this::resolveListForLocal);
    }

    /**
     * 查询所有银行列表
     *
     * @return List<Bank>
     */
    public List<Bank> listFromRedis() {
        Map<String, String> entries = redisHash.entries(RedisKeyEnum.BANK.getVal());
        if (CollectionUtil.isEmpty(entries)) {
            return new ArrayList<>();
        }

        Collection<String> valuesStrList = entries.values();

        return CollectionUtil.toList(valuesStrList, s -> JSON.parseObject(s, Bank.class));
    }

    /**
     * 根据ID查询银行
     *
     * @param id 银行ID
     * @return Bank
     */
    public Bank getByIdFromRedis(long id) {
        String str = redisHash.get(RedisKeyEnum.BANK.getVal(), id + "");
        if (StrUtil.isBlank(str)) {
            return null;
        }

        return JSON.parseObject(str, Bank.class);
    }

    /**
     * 根据编码查询银行
     *
     * @param code
     * @return Bank
     */
    public Bank getByCodeFromRedis(String code) {
        Long bankId = getIdByCodeFromLocal(code);
        if (bankId == null) {
            return null;
        }

        return getByIdFromRedis(bankId);
    }

    /**
     * 根据ID集合查询银行集合
     */
    public List<Bank> listByIdsFromRedis(Collection<String> ids) {
        List<String> values = redisHash.multiGet(RedisKeyEnum.BANK.getVal(), ids);
        if (CollectionUtil.isEmpty(values)) {
            return new ArrayList<>();
        }

        return CollectionUtil.toList(values, value -> JSON.parseObject(value, Bank.class));
    }

    public String getNameByIdFromLocal(Long id) {
        if (id == null) {
            return null;
        }
        return getNameByIdFromLocal(id, false);
    }

    public String getCodeByIdFromLocal(Long id) {
        if (id == null) {
            return null;
        }
        return LOCAL_ID_CODE_CACHE.get(id);
    }

    public String getNameByIdFromLocal(long id, boolean deepGet) {
        String name = LOCAL_ID_NAME_CACHE.get(id);

        if (name == null && deepGet) {
            LambdaQueryWrapper<Bank> wrapper = new LambdaQueryWrapper<Bank>().eq(Bank::getId, id);
            Bank one = bankService.getOne(wrapper);
            if (one != null) {
                // 这里是I18N值，可以用来翻译
                name = one.getName();
                LOCAL_ID_NAME_CACHE.put(id, name);
            }
        }

        return name;
    }

    public Long getIdByCodeFromLocal(String code) {
        return LOCAL_CODE_ID_CACHE.get(code);
    }

    public String getNameByCodeFromLocal(String code) {
        return LOCAL_CODE_NAME_CACHE.get(code);
    }

    /**
     * 加载并缓存数据
     *
     * @param consumer 处理方法
     */
    private void loadAndCache(Consumer<List<Bank>> consumer) {
        List<Bank> list = bankService.lambdaQuery().orderByDesc(Bank::getSort).list();

        consumer.accept(list);
    }

    /**
     * 解析并缓存数据
     *
     * @param list 银行列表
     */
    private void resolveListForLocal(List<Bank> list) {
        // 分组
        Map<Long, String> idNameMap = CollectionUtil.toMapWithKeyValue(list, Bank::getId, bank -> bank.getName());
        Map<Long, String> idCodeMap = CollectionUtil.toMapWithKeyValue(list, Bank::getId, bank -> bank.getCode());
        Map<String, Long> codeIdMap = CollectionUtil.toMapWithKeyValue(list, Bank::getCode, bank -> bank.getId());
        Map<String, String> codeNameMap = CollectionUtil.toMapWithKeyValue(list, Bank::getCode, bank -> bank.getName());

        LOCAL_ID_NAME_CACHE = idNameMap;
        LOCAL_ID_CODE_CACHE = idCodeMap;
        LOCAL_CODE_ID_CACHE = codeIdMap;
        LOCAL_CODE_NAME_CACHE = codeNameMap;
    }

    /**
     * 解析并缓存数据
     *
     * @param list 银行列表
     */
    private void resolveListForRedis(List<Bank> list) {
        // 分组
        Map<Long, Bank> idMap = CollectionUtil.toMapWithKey(list, Bank::getId);

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
                byte[] key = keySerializer.serialize(RedisKeyEnum.BANK.getVal());
                connection.del(key);
                if (CollectionUtil.isNotEmpty(redisData)) {
                    connection.hMSet(key, redisData);
                }
            }

            return null;
        });
    }
}
