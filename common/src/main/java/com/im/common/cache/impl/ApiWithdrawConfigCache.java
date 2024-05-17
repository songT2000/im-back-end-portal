package com.im.common.cache.impl;

import com.alibaba.fastjson.JSON;
import com.im.common.cache.base.BaseCacheHandler;
import com.im.common.cache.base.CacheProperty;
import com.im.common.constant.RedisKeyEnum;
import com.im.common.entity.ApiWithdrawConfig;
import com.im.common.entity.enums.ApiWithdrawConfigCodeEnum;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.service.ApiWithdrawConfigService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.ip.IpAddressUtil;
import com.im.common.util.redis.RedisKeySerializer;
import com.im.common.util.redis.RedisValueSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * <p>API代付配置缓存</p>
 *
 * @author Barry
 * @date 2020-05-23
 */
@CacheProperty(value = SysCacheRefreshTypeEnum.API_WITHDRAW_CONFIG, redis = true, local = true)
@Component
public class ApiWithdrawConfigCache implements BaseCacheHandler {
    private ApiWithdrawConfigService apiWithdrawConfigService;
    private HashOperations<String, String, String> redisHash;
    private RedisKeySerializer keySerializer;
    private RedisValueSerializer valueSerializer;

    private Map<Long, String> LOCAL_ID_NAME_CACHE = new HashMap<>();
    private Map<Long, ApiWithdrawConfigCodeEnum> LOCAL_ID_CODE_CACHE = new HashMap<>();
    private Map<Long, String> LOCAL_ID_THIRD_CALLBACK_WHITELIST_IP = new HashMap<>();

    @Autowired
    public void setApiWithdrawConfigService(ApiWithdrawConfigService apiWithdrawConfigService) {
        this.apiWithdrawConfigService = apiWithdrawConfigService;
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

    public ApiWithdrawConfigCodeEnum getCodeByIdFromLocal(Long id) {
        if (id == null) {
            return null;
        }
        return LOCAL_ID_CODE_CACHE.get(id);
    }

    /**
     * 如果没有设置，则不校验
     *
     * @param id
     * @return
     */
    public boolean isWhitelistCallbackIp(Long id, String ip) {
        String whitelist = LOCAL_ID_THIRD_CALLBACK_WHITELIST_IP.get(id);

        if (StrUtil.isBlank(whitelist)) {
            // 没有设置，不限制
            return true;
        }

        // 直接匹配
        if (whitelist.contains(ip)) {
            return true;
        }

        return IpAddressUtil.isInMultipleRangeOrMask(whitelist, ip);
    }

    /**
     * 获取白名单IP
     *
     * @param id
     * @return
     */
    public String getWhitelistCallbackIp(Long id) {
        return LOCAL_ID_THIRD_CALLBACK_WHITELIST_IP.get(id);
    }

    /**
     * 列出可用的API代付配置，返回启用的，会排序
     *
     * @return
     */
    public List<ApiWithdrawConfig> listEnabledFromRedisForAdmin() {
        Map<String, String> entries = redisHash.entries(RedisKeyEnum.API_WITHDRAW_CONFIG.getVal());
        if (CollectionUtil.isEmpty(entries)) {
            return new ArrayList<>();
        }

        Collection<String> valuesStrList = entries.values();

        // 过滤本身数据可用的
        List<ApiWithdrawConfig> list = CollectionUtil.toList(valuesStrList, s -> JSON.parseObject(s, ApiWithdrawConfig.class));
        list = CollectionUtil.filterList(list, this::isAvailableConfig);

        list = CollectionUtil.sort(list, Comparator.comparingInt(ApiWithdrawConfig::getSort));

        return list;
    }

    /**
     * 没有过滤能不能用
     *
     * @param id
     * @return
     */
    public ApiWithdrawConfig getByIdFromRedis(Long id) {
        if (id == null) {
            return null;
        }

        String str = redisHash.get(RedisKeyEnum.API_WITHDRAW_CONFIG.getVal(), id + "");
        if (StrUtil.isBlank(str)) {
            return null;
        }

        return JSON.parseObject(str, ApiWithdrawConfig.class);
    }

    /**
     * 配置是否可用
     *
     * @param config
     * @return
     */
    public boolean isAvailableConfig(ApiWithdrawConfig config) {
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
        List<ApiWithdrawConfig> list = apiWithdrawConfigService.list();

        // 本地缓存
        {
            Map<Long, String> idNameMap = CollectionUtil.toMapWithKeyValue(list, e -> e.getId(), e -> e.getName());
            Map<Long, ApiWithdrawConfigCodeEnum> idCodeMap = CollectionUtil.toMapWithKeyValue(list, e -> e.getCode() != null, e -> e.getId(), e -> e.getCode());
            Map<Long, String> callbackWhitelistIpMap = CollectionUtil.toMapWithKeyValue(CollectionUtil.filterList(list,
                            e -> StrUtil.isNotBlank(e.getThirdCallbackWhitelistIp())),
                    e -> e.getId(), e -> e.getThirdCallbackWhitelistIp());

            this.LOCAL_ID_NAME_CACHE = idNameMap;
            this.LOCAL_ID_CODE_CACHE = idCodeMap;
            this.LOCAL_ID_THIRD_CALLBACK_WHITELIST_IP = callbackWhitelistIpMap;
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
                    byte[] key = keySerializer.serialize(RedisKeyEnum.API_WITHDRAW_CONFIG.getVal());
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
