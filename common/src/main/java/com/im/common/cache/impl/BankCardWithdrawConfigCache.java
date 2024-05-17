package com.im.common.cache.impl;

import com.alibaba.fastjson.JSON;
import com.im.common.cache.base.BaseCacheHandler;
import com.im.common.cache.base.CacheProperty;
import com.im.common.constant.RedisKeyEnum;
import com.im.common.entity.BankCardWithdrawConfig;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.service.BankCardWithdrawConfigService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.redis.RedisKeySerializer;
import com.im.common.util.redis.RedisValueSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * <p>银行卡提现配置缓存</p>
 *
 * @author Barry
 * @date 2020-05-23
 */
@CacheProperty(value = SysCacheRefreshTypeEnum.BANK_CARD_WITHDRAW_CONFIG, redis = true, local = true)
@Component
public class BankCardWithdrawConfigCache implements BaseCacheHandler {
    private BankCardWithdrawConfigService bankCardWithdrawConfigService;
    private HashOperations<String, String, String> redisHash;
    private RedisKeySerializer keySerializer;
    private RedisValueSerializer valueSerializer;

    private Map<Long, String> LOCAL_ID_NAME_CACHE = new HashMap<>();

    @Autowired
    public void setBankCardWithdrawConfigService(BankCardWithdrawConfigService bankCardWithdrawConfigService) {
        this.bankCardWithdrawConfigService = bankCardWithdrawConfigService;
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
    public void reloadLocal() {
        loadAndCache();
    }

    @Override
    public void reloadRedis() {
        loadAndCache();
    }

    public String getNameByIdFromLocal(Long id) {
        if (id == null) {
            return null;
        }
        return LOCAL_ID_NAME_CACHE.get(id);
    }

    /**
     * 列出可用的银行卡提现配置，返回启用的，会排序
     *
     * @return
     */
    public List<BankCardWithdrawConfig> listEnabledFromRedisForPortal() {
        Map<String, String> entries = redisHash.entries(RedisKeyEnum.BANK_CARD_WITHDRAW_CONFIG.getVal());
        if (CollectionUtil.isEmpty(entries)) {
            return new ArrayList<>();
        }

        Collection<String> valuesStrList = entries.values();

        // 过滤本身数据可用的
        List<BankCardWithdrawConfig> list = CollectionUtil.toList(valuesStrList, s -> JSON.parseObject(s, BankCardWithdrawConfig.class));
        list = CollectionUtil.filterList(list, this::isAvailableConfig);

        list = CollectionUtil.sort(list, Comparator.comparingInt(BankCardWithdrawConfig::getSort));

        return list;
    }

    /**
     * 没有过滤能不能用
     *
     * @param id
     * @return
     */
    public BankCardWithdrawConfig getByIdFromRedis(Long id) {
        if (id == null) {
            return null;
        }

        String str = redisHash.get(RedisKeyEnum.BANK_CARD_WITHDRAW_CONFIG.getVal(), id + "");
        if (StrUtil.isBlank(str)) {
            return null;
        }

        return JSON.parseObject(str, BankCardWithdrawConfig.class);
    }

    /**
     * 配置是否可用
     *
     * @param config
     * @return
     */
    public boolean isAvailableConfig(BankCardWithdrawConfig config) {
        if (!Boolean.TRUE.equals(config.getEnabled())) {
            return false;
        }
        if (Boolean.TRUE.equals(config.getDeleted())) {
            return false;
        }
        return true;
    }

    /**
     * 加载并缓存数据
     */
    private void loadAndCache() {
        List<BankCardWithdrawConfig> list = bankCardWithdrawConfigService.list();

        // 本地缓存
        {
            Map<Long, String> idNameMap = CollectionUtil.toMapWithKeyValue(list, e -> e.getId(), e -> e.getName());

            LOCAL_ID_NAME_CACHE = idNameMap;
        }

        // redis缓存
        {
            // 组装redis数据
            Map<byte[], byte[]> redisIdData = CollectionUtil.toMapWithKeyValue(list,
                    e -> keySerializer.serialize(e.getId()),
                    e -> valueSerializer.serialize(e));

            // 设置到redis，使用pipeline减少删增之间的真空时间
            redisHash.getOperations().executePipelined((RedisCallback<Object>) connection -> {
                {
                    byte[] key = keySerializer.serialize(RedisKeyEnum.BANK_CARD_WITHDRAW_CONFIG.getVal());
                    connection.del(key);
                    if (CollectionUtil.isNotEmpty(redisIdData)) {
                        connection.hMSet(key, redisIdData);
                    }
                }
                return null;
            });
        }
    }
}
