package com.im.common.cache.impl;

import com.alibaba.fastjson.JSON;
import com.im.common.cache.base.BaseCacheHandler;
import com.im.common.cache.base.CacheProperty;
import com.im.common.cache.sysconfig.SysConfigResolverProxy;
import com.im.common.cache.sysconfig.bo.*;
import com.im.common.constant.RedisKeyEnum;
import com.im.common.entity.SysConfig;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.entity.enums.SysConfigGroupEnum;
import com.im.common.service.SysConfigService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.redis.RedisKeySerializer;
import com.im.common.util.redis.RedisValueSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统配置缓存
 * <p>
 * 如在sys_config中新增了新的group，则在{@link com.im.common.cache.sysconfig.resolver}该包下新增resolver即可
 *
 * @author Barry
 * @date 2018/6/8
 */
@CacheProperty(value = SysCacheRefreshTypeEnum.SYS_CONFIG, redis = true, local = true)
@Component
public class SysConfigCache implements BaseCacheHandler {
    private SysConfigService sysConfigService;
    private HashOperations<String, String, String> redisHash;
    private RedisKeySerializer keySerializer;
    private RedisValueSerializer valueSerializer;

    private Map<SysConfigGroupEnum, BaseSysConfigBO> LOCAL_MAP = new HashMap<>();

    @Autowired
    public void setSysConfigService(SysConfigService sysConfigService) {
        this.sysConfigService = sysConfigService;
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

    /**
     * 获取全局配置
     *
     * @return GlobalConfigBO
     */
    public GlobalConfigBO getGlobalConfigFromRedis() {
        return getConfigFromRedis(SysConfigGroupEnum.GLOBAL, GlobalConfigBO.class);
    }

    /**
     * 获取全局配置
     *
     * @return GlobalConfigBO
     */
    public GlobalConfigBO getGlobalConfigFromLocal() {
        return (GlobalConfigBO) LOCAL_MAP.get(SysConfigGroupEnum.GLOBAL);
    }

    /**
     * 获取后台配置
     *
     * @return AdminConfigCacheBO
     */
    public AdminConfigBO getAdminConfigFromRedis() {
        return getConfigFromRedis(SysConfigGroupEnum.ADMIN, AdminConfigBO.class);
    }

    /**
     * 获取后台配置
     *
     * @return AdminConfigCacheBO
     */
    public AdminConfigBO getAdminConfigFromLocal() {
        return (AdminConfigBO) LOCAL_MAP.get(SysConfigGroupEnum.ADMIN);
    }

    /**
     * 获取前台配置
     *
     * @return PortalConfigBO
     */
    public PortalConfigBO getPortalConfigFromRedis() {
        return getConfigFromRedis(SysConfigGroupEnum.PORTAL, PortalConfigBO.class);
    }

    /**
     * 获取前台配置
     *
     * @return PortalConfigBO
     */
    public PortalConfigBO getPortalConfigFromLocal() {
        return (PortalConfigBO) LOCAL_MAP.get(SysConfigGroupEnum.PORTAL);
    }

    /**
     * 获取注册配置
     *
     * @return RegisterConfigBO
     */
    public RegisterConfigBO getRegisterConfigFromRedis() {
        return getConfigFromRedis(SysConfigGroupEnum.REGISTER, RegisterConfigBO.class);
    }

    /**
     * 获取充值配置
     *
     * @return RechargeConfigBO
     */
    public RechargeConfigBO getRechargeConfigFromRedis() {
        return getConfigFromRedis(SysConfigGroupEnum.RECHARGE, RechargeConfigBO.class);
    }

    /**
     * 获取提现配置
     *
     * @return WithdrawConfigBO
     */
    public WithdrawConfigBO getWithdrawConfigFromRedis() {
        return getConfigFromRedis(SysConfigGroupEnum.WITHDRAW, WithdrawConfigBO.class);
    }

    /**
     * 获取报表配置
     *
     * @return ReportConfigBO
     */
    public ReportConfigBO getReportConfigFromRedis() {
        return getConfigFromRedis(SysConfigGroupEnum.REPORT, ReportConfigBO.class);
    }

    /**
     * 获取报表配置
     *
     * @return ReportConfigBO
     */
    public ReportConfigBO getReportConfigFromLocal() {
        return (ReportConfigBO) LOCAL_MAP.get(SysConfigGroupEnum.REPORT);
    }

    /**
     * 获取OSS配置
     *
     * @return OssConfigBO
     */
    public OssConfigBO getOssConfigFromRedis() {
        return getConfigFromRedis(SysConfigGroupEnum.OSS, OssConfigBO.class);
    }

    /**
     * 获取MQTT配置
     *
     * @return MqttConfigBO
     */
    public MqttConfigBO getMqttConfigFromLocal() {
        return (MqttConfigBO) LOCAL_MAP.get(SysConfigGroupEnum.MQTT);
    }

    /**
     * 获取IM配置
     *
     * @return ImConfigBO
     */
    public ImConfigBO getImConfigFromLocal() {
        return (ImConfigBO) LOCAL_MAP.get(SysConfigGroupEnum.IM);
    }

    /**
     * 获取红包配置
     *
     * @return RedEnvelopeConfigBO
     */
    public RedEnvelopeConfigBO getRedEnvelopeConfigFromRedis() {
        return getConfigFromRedis(SysConfigGroupEnum.RED_ENVELOPE, RedEnvelopeConfigBO.class);
    }

    private <T extends BaseSysConfigBO> T getConfigFromRedis(SysConfigGroupEnum group, Class<T> clazz) {
        String cacheStr = redisHash.get(RedisKeyEnum.SYS_CONFIG.getVal(), group);
        if (cacheStr == null) {
            return (T) getDefaultConfig(group);
        }
        return JSON.parseObject(cacheStr, clazz);
    }

    @Override
    public void init() {
        SysConfigResolverProxy.initResolvers();

        Instant start = beforeInit();

        this.reloadRedis();
        this.reloadLocal();

        afterInit(start);
    }

    @Override
    public void reloadRedis() {
        loadAndCache();
    }

    @Override
    public void reloadLocal() {
        loadAndCache();
    }

    /**
     * 加载/解析并缓存数据
     */
    private void loadAndCache() {
        List<SysConfig> sysConfigs = sysConfigService.list();

        // 数据库映射
        Optional.ofNullable(sysConfigs).ifPresent(this::resolveList);

        // 把数据库没有的配置项初始化默认值
        resolveDefaultList(sysConfigs);
    }

    /**
     * 解析并缓存数据
     *
     * @param sysConfigs 配置列表
     */
    private void resolveList(List<SysConfig> sysConfigs) {
        // 解析数据
        Map<SysConfigGroupEnum, BaseSysConfigBO> resolveData = SysConfigResolverProxy.resolve(sysConfigs);

        if (CollectionUtil.isNotEmpty(resolveData)) {

            // 组装redis数据

            Map<byte[], byte[]> redisData = resolveData.entrySet().stream().collect(Collectors.toMap(entry -> keySerializer.serialize(entry.getKey()), entry -> valueSerializer.serialize(entry.getValue())));

            // 设置到redis，使用pipeline减少删增之间的真空时间
            redisHash.getOperations().executePipelined((RedisCallback<Object>) connection -> {
                {
                    byte[] key = keySerializer.serialize(RedisKeyEnum.SYS_CONFIG.getVal());
                    connection.del(key);
                    if (CollectionUtil.isNotEmpty(redisData)) {
                        connection.hMSet(key, redisData);
                    }
                }

                return null;
            });
        }

        LOCAL_MAP = resolveData;
    }

    /**
     * 把数据库没有的配置项初始化,并使用默认值,防止找不到数据的情况
     */
    private void resolveDefaultList(List<SysConfig> sysConfigs) {
        // 把数据库没有的配置项初始化,并使用默认值,防止找不到数据的情况
        SysConfigGroupEnum[] groupEnums = SysConfigGroupEnum.values();
        if (CollectionUtil.isEmpty(groupEnums)) {
            return;
        }

        // 数据库的
        Set<SysConfigGroupEnum> dbGroups = CollectionUtil.toSet(sysConfigs, sysConfig -> sysConfig.getGroup());

        // 实际应该要有的
        Set<SysConfigGroupEnum> realGroups = CollectionUtil.toSet(groupEnums);

        // 求差集，实际比数据库多出的，就是数据库中不存在的
        Set<SysConfigGroupEnum> difference = CollectionUtil.difference(dbGroups, realGroups);

        if (difference != null) {
            for (SysConfigGroupEnum groupEnum : difference) {
                BaseSysConfigBO emptyConfig = getDefaultConfig(groupEnum);
                redisHash.put(RedisKeyEnum.SYS_CONFIG.getVal(), groupEnum.toString(), JSON.toJSONString(emptyConfig));
            }
        }
    }

    private BaseSysConfigBO getDefaultConfig(SysConfigGroupEnum groupEnum) {
        return SysConfigResolverProxy.getDefault(groupEnum);
    }
}
