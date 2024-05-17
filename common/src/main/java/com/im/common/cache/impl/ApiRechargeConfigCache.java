package com.im.common.cache.impl;

import com.alibaba.fastjson.JSON;
import com.im.common.cache.base.BaseCacheHandler;
import com.im.common.cache.base.CacheProperty;
import com.im.common.constant.RedisKeyEnum;
import com.im.common.entity.ApiRechargeConfig;
import com.im.common.entity.enums.ApiRechargeConfigCodeEnum;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.ApiRechargeConfigService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.DateTimeUtil;
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
 * <p>三方充值配置缓存</p>
 *
 * @author Barry
 * @date 2020-05-23
 */
@CacheProperty(value = SysCacheRefreshTypeEnum.API_RECHARGE_CONFIG, redis = true, local = true)
@Component
public class ApiRechargeConfigCache implements BaseCacheHandler {
    private ApiRechargeConfigService apiRechargeConfigService;
    private HashOperations<String, String, String> redisHash;
    private RedisKeySerializer keySerializer;
    private RedisValueSerializer valueSerializer;

    private Map<Long, String> LOCAL_ID_ADMIN_NAME_CACHE = new HashMap<>();
    private Map<Long, String> LOCAL_ID_PORTAL_NAME_CACHE = new HashMap<>();
    private Map<Long, ApiRechargeConfigCodeEnum> LOCAL_ID_CODE_CACHE = new HashMap<>();
    private Map<Long, String> LOCAL_ID_THIRD_CALLBACK_WHITELIST_IP = new HashMap<>();

    @Autowired
    public void setApiRechargeConfigService(ApiRechargeConfigService apiRechargeConfigService) {
        this.apiRechargeConfigService = apiRechargeConfigService;
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

    public String getAdminNameByIdFromLocal(Long id) {
        if (id == null) {
            return null;
        }
        return LOCAL_ID_ADMIN_NAME_CACHE.get(id);
    }

    public String getPortalNameByIdFromLocal(Long id) {
        if (id == null) {
            return null;
        }
        return LOCAL_ID_PORTAL_NAME_CACHE.get(id);
    }

    public ApiRechargeConfigCodeEnum getCodeByIdFromLocal(Long id) {
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
     * 列出可用的三方充值配置，返回启用的，且在服务时间内的，会排序
     *
     * @param userId
     * @param userGroupUserCache
     * @param userGroupApiRechargeConfigCache
     * @return
     */
    public List<ApiRechargeConfig> listEnabledFromRedisForPortal(long userId, UserGroupUserCache userGroupUserCache, UserGroupApiRechargeConfigCache userGroupApiRechargeConfigCache) {
        Map<String, String> entries = redisHash.entries(RedisKeyEnum.API_RECHARGE_CONFIG.getVal());
        if (CollectionUtil.isEmpty(entries)) {
            return new ArrayList<>();
        }

        Collection<String> valuesStrList = entries.values();

        // 过滤本身数据可用的
        List<ApiRechargeConfig> list = CollectionUtil.toList(valuesStrList, s -> JSON.parseObject(s, ApiRechargeConfig.class));
        list = CollectionUtil.filterList(list, this::isAvailableConfig);

        // 过滤用户组
        List<ApiRechargeConfig> filterList = new ArrayList<>();
        for (ApiRechargeConfig data : list) {
            Set<Long> dataGroupSet = userGroupApiRechargeConfigCache.getGroupSetByApiRechargeConfigIdFromLocal(data.getId());
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
        filterList = CollectionUtil.sort(filterList, Comparator.comparingInt(ApiRechargeConfig::getSort));

        return filterList;
    }

    /**
     * 找到可用的配置
     * 已处理用户组数据
     *
     * @param id
     * @param userId
     * @param userGroupUserCache
     * @param userGroupApiRechargeChannelCache
     * @return
     */
    public RestResponse<ApiRechargeConfig> getEnabledFromRedisForPortal(Long id,
                                                                        long userId,
                                                                        UserGroupUserCache userGroupUserCache,
                                                                        UserGroupApiRechargeConfigCache userGroupApiRechargeChannelCache) {
        if (id == null) {
            return RestResponse.failed(ResponseCode.SYS_DATA_NOT_EXIST);
        }

        String str = redisHash.get(RedisKeyEnum.API_RECHARGE_CONFIG.getVal(), id + "");
        if (StrUtil.isBlank(str)) {
            return RestResponse.failed(ResponseCode.SYS_DATA_NOT_EXIST);
        }

        // 过滤本身数据可用的
        ApiRechargeConfig config = JSON.parseObject(str, ApiRechargeConfig.class);
        if (!isAvailableConfig(config)) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        // 过滤用户组
        Set<Long> dataGroupSet = userGroupApiRechargeChannelCache.getGroupSetByApiRechargeConfigIdFromLocal(config.getId());
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
    public ApiRechargeConfig getByIdFromRedis(Long id) {
        if (id == null) {
            return null;
        }

        String str = redisHash.get(RedisKeyEnum.API_RECHARGE_CONFIG.getVal(), id + "");
        if (StrUtil.isBlank(str)) {
            return null;
        }

        return JSON.parseObject(str, ApiRechargeConfig.class);
    }

    /**
     * 配置是否可用
     *
     * @param config
     * @return
     */
    public boolean isAvailableConfig(ApiRechargeConfig config) {
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
        List<ApiRechargeConfig> list = apiRechargeConfigService.list();

        // 本地缓存
        {
            Map<Long, String> idAdminNameMap = CollectionUtil.toMapWithKeyValue(list, e -> e.getId(), e -> e.getAdminName());
            Map<Long, String> idPortalNameMap = CollectionUtil.toMapWithKeyValue(list, e -> e.getId(), e -> e.getPortalName());
            Map<Long, ApiRechargeConfigCodeEnum> idCodeMap = CollectionUtil.toMapWithKeyValue(list, e -> e.getCode() != null, e -> e.getId(), e -> e.getCode());
            Map<Long, String> callbackWhitelistIpMap = CollectionUtil.toMapWithKeyValue(CollectionUtil.filterList(list,
                            e -> StrUtil.isNotBlank(e.getThirdCallbackWhitelistIp())),
                    e -> e.getId(), e -> e.getThirdCallbackWhitelistIp());

            this.LOCAL_ID_ADMIN_NAME_CACHE = idAdminNameMap;
            this.LOCAL_ID_PORTAL_NAME_CACHE = idPortalNameMap;
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
                    byte[] key = keySerializer.serialize(RedisKeyEnum.API_RECHARGE_CONFIG.getVal());
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
