package com.im.common.cache.impl;

import com.alibaba.fastjson.JSON;
import com.im.common.cache.base.BaseCacheHandler;
import com.im.common.cache.base.CacheProperty;
import com.im.common.constant.RedisKeyEnum;
import com.im.common.entity.BankCardRechargeConfig;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.BankCardRechargeConfigService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.DateTimeUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.redis.RedisKeySerializer;
import com.im.common.util.redis.RedisValueSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * <p>银行卡充值配置缓存</p>
 *
 * @author Barry
 * @date 2020-05-23
 */
@CacheProperty(value = SysCacheRefreshTypeEnum.BANK_CARD_RECHARGE_CONFIG, redis = true, local = true)
@Component
public class BankCardRechargeConfigCache implements BaseCacheHandler {
    private BankCardRechargeConfigService bankCardRechargeConfigService;
    private HashOperations<String, String, String> redisHash;
    private RedisKeySerializer keySerializer;
    private RedisValueSerializer valueSerializer;

    private Map<Long, String> LOCAL_ID_NAME_CACHE = new HashMap<>();

    @Autowired
    public void setBankCardRechargeConfigService(BankCardRechargeConfigService bankCardRechargeConfigService) {
        this.bankCardRechargeConfigService = bankCardRechargeConfigService;
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
     * 列出可用的银行卡充值配置，返回启用的，且在服务时间内的，会排序
     *
     * @param userId
     * @param userGroupUserCache
     * @param userGroupBankCardRechargeConfigCache
     * @return
     */
    public List<BankCardRechargeConfig> listEnabledFromRedisForPortal(long userId, UserGroupUserCache userGroupUserCache, UserGroupBankCardRechargeConfigCache userGroupBankCardRechargeConfigCache) {
        Map<String, String> entries = redisHash.entries(RedisKeyEnum.BANK_CARD_RECHARGE_CONFIG.getVal());
        if (CollectionUtil.isEmpty(entries)) {
            return new ArrayList<>();
        }

        Collection<String> valuesStrList = entries.values();

        // 过滤本身数据可用的
        List<BankCardRechargeConfig> list = CollectionUtil.toList(valuesStrList, s -> JSON.parseObject(s, BankCardRechargeConfig.class));
        list = CollectionUtil.filterList(list, this::isAvailableConfig);

        // 过滤用户组
        List<BankCardRechargeConfig> filterList = new ArrayList<>();
        for (BankCardRechargeConfig data : list) {
            Set<Long> dataGroupSet = userGroupBankCardRechargeConfigCache.getGroupSetByBankCardRechargeConfigIdFromLocal(data.getId());
            if (CollectionUtil.isEmpty(dataGroupSet)) {
                // 如果该数据未关联用户组，则所有人可用
                filterList.add(data);
            } else {
                // 如果该数据已关联用户组，则只能组内用户可用
                boolean hasAnyGroup = userGroupUserCache.hasAnyGroupByUserIdFromLocal(userId, dataGroupSet);
                if (hasAnyGroup) {
                    filterList.add(data);
                }
            }
        }

        // 排序
        filterList = CollectionUtil.sort(filterList, Comparator.comparingInt(BankCardRechargeConfig::getSort));

        return filterList;
    }

    /**
     * 找到可用的配置
     * 已处理用户组数据
     *
     * @param id
     * @param userId
     * @param userGroupUserCache
     * @param userGroupBankCardRechargeConfigCache
     * @return
     */
    public RestResponse<BankCardRechargeConfig> getEnabledFromRedisForPortal(Long id,
                                                                             long userId,
                                                                             UserGroupUserCache userGroupUserCache,
                                                                             UserGroupBankCardRechargeConfigCache userGroupBankCardRechargeConfigCache) {
        if (id == null) {
            return RestResponse.failed(ResponseCode.SYS_DATA_NOT_EXIST);
        }

        String str = redisHash.get(RedisKeyEnum.BANK_CARD_RECHARGE_CONFIG.getVal(), id + "");
        if (StrUtil.isBlank(str)) {
            return RestResponse.failed(ResponseCode.SYS_DATA_NOT_EXIST);
        }

        // 过滤本身数据可用的
        BankCardRechargeConfig config = JSON.parseObject(str, BankCardRechargeConfig.class);
        if (!isAvailableConfig(config)) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        // 过滤用户组
        Set<Long> dataGroupSet = userGroupBankCardRechargeConfigCache.getGroupSetByBankCardRechargeConfigIdFromLocal(config.getId());
        if (CollectionUtil.isNotEmpty(dataGroupSet)) {
            // 如果该数据已关联用户组，则只能组内用户可用
            boolean hasAnyGroup = userGroupUserCache.hasAnyGroupByUserIdFromLocal(userId, dataGroupSet);
            if (!hasAnyGroup) {
                return RestResponse.failed(ResponseCode.SYS_DATA_NOT_EXIST);
            }
        }

        return RestResponse.ok(config);
    }

    /**
     * 没有过滤能不能用
     *
     * @param id
     * @return
     */
    public BankCardRechargeConfig getByIdFromRedis(Long id) {
        if (id == null) {
            return null;
        }

        String str = redisHash.get(RedisKeyEnum.BANK_CARD_RECHARGE_CONFIG.getVal(), id + "");
        if (StrUtil.isBlank(str)) {
            return null;
        }

        return JSON.parseObject(str, BankCardRechargeConfig.class);
    }

    /**
     * 配置是否可用
     *
     * @param config
     * @return
     */
    public boolean isAvailableConfig(BankCardRechargeConfig config) {
        if (!Boolean.TRUE.equals(config.getEnabled())) {
            return false;
        }
        if (Boolean.TRUE.equals(config.getDeleted())) {
            return false;
        }
        if (!DateTimeUtil.isDuringServiceTime(config.getEnableTime())) {
            return false;
        }
        return true;
    }

    /**
     * 加载并缓存数据
     */
    private void loadAndCache() {
        List<BankCardRechargeConfig> list = bankCardRechargeConfigService.list();

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
                    byte[] key = keySerializer.serialize(RedisKeyEnum.BANK_CARD_RECHARGE_CONFIG.getVal());
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
