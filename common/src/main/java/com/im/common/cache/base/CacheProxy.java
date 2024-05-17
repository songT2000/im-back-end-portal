package com.im.common.cache.base;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.im.common.cache.base.bo.SysCacheRefreshBO;
import com.im.common.config.RedisConfig;
import com.im.common.config.SpringConfig;
import com.im.common.constant.RedisKeyEnum;
import com.im.common.entity.SysCacheRefresh;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.util.ApplicationUtil;
import com.im.common.util.CollectionUtil;
import com.im.common.util.DateTimeUtil;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import com.im.common.util.redis.RedisKeySerializer;
import com.im.common.util.redis.RedisValueSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 缓存代理
 *
 * <p>
 * 应用使用规则
 * 1：如果需要维护所有缓存（刷新缓存），则调用{@link #checkRedisCache}
 *
 * @author Barry
 * @date 2018/6/8
 */
@Component
@ConditionalOnBean({SpringConfig.class, RedisConfig.class})
public class CacheProxy {

    private static final Log LOG = LogFactory.get();

    private ApplicationContext applicationContext;
    private CacheFactory cacheFactory;
    private MyBatisPlusService<SysCacheRefresh> sysCacheRefreshService;
    private HashOperations<String, String, String> redisHash;
    private RedisKeySerializer keySerializer;
    private RedisValueSerializer valueSerializer;

    /**
     * 定时器
     */
    private ScheduledExecutorService localRefreshTimer;

    /**
     * 上次刷新本地缓存时间
     */
    private Map<SysCacheRefreshTypeEnum, LocalDateTime> LOCAL_LAST_UPDATE_TIME = new HashMap<>();

    @Autowired
    public CacheProxy(ApplicationContext applicationContext,
                      MyBatisPlusService<SysCacheRefresh> sysCacheRefreshService,
                      HashOperations<String, String, String> redisHash,
                      RedisKeySerializer keySerializer,
                      RedisValueSerializer valueSerializer) {
        this.applicationContext = applicationContext;
        this.sysCacheRefreshService = sysCacheRefreshService;
        this.redisHash = redisHash;
        this.keySerializer = keySerializer;
        this.valueSerializer = valueSerializer;
    }

    /**
     * 初始化缓存相关
     *
     * @param schedulerRedisRefresh 是否调度刷新redis缓存，即@CacheProperty(redis=true)的数据，如该值为true，必须把所有缓存对象显式声明
     */
    public void init(boolean schedulerRedisRefresh) {
        init(schedulerRedisRefresh, null);
    }

    /**
     * 初始化缓存相关
     *
     * @param schedulerRedisRefresh 是否调度刷新redis缓存，即@CacheProperty(redis=true)的数据，如该值为true，必须把所有缓存对象显式声明
     * @param consumer              哪个缓存发生变化时，会传过来
     */
    public void init(boolean schedulerRedisRefresh, Consumer<SysCacheRefreshTypeEnum> consumer) {
        cacheFactory = new CacheFactory(applicationContext);
        cacheFactory.init();

        // 初始化所有缓存刷新时间
        initCacheRefreshTime();

        // 缓存检查调度
        scheduledRefresh(schedulerRedisRefresh, consumer);
    }

    /**
     * 标记需要刷新的缓存，只会更新数据库的updateTime，手动调用此方法
     */
    public void signalRefreshCache(SysCacheRefreshTypeEnum typeEnum) {
        LocalDateTime now = LocalDateTime.now();
        // 设置数据库updateTime
        sysCacheRefreshService.update(new LambdaUpdateWrapper<SysCacheRefresh>().eq(SysCacheRefresh::getType, typeEnum)
                .set(SysCacheRefresh::getUpdateTime, now));
    }

    /**
     * 初始化一个每3秒检查一次缓存的调度
     *
     * @param schedulerRedisRefresh 是否调度刷新redis缓存，即@CacheProperty(redis=true)的数据
     */
    private void scheduledRefresh(boolean schedulerRedisRefresh, Consumer<SysCacheRefreshTypeEnum> consumer) {
        if (localRefreshTimer == null) {
            localRefreshTimer = new ScheduledThreadPoolExecutor(2,
                    r -> ThreadUtil.newThread(r, StrUtil.format("Local-Cache-Refresh-Timer")));
        }

        // 调度周期
        long periodMs = Duration.ofSeconds(3).toMillis();

        // 创建调度
        localRefreshTimer.scheduleAtFixedRate(() -> {
            try {
                getDBRefreshTime(dbTimeMap -> {
                    if (schedulerRedisRefresh) {
                        this.checkRedisCache(dbTimeMap);
                    }

                    this.checkLocalCache(dbTimeMap, consumer);
                });
            } catch (Exception e) {
            }
        }, periodMs, periodMs, TimeUnit.MILLISECONDS);
    }

    /**
     * 检查各项Redis缓存是否需要刷新
     * <p>
     * 如果本项目没有声明相应缓存处理类为spring对象，则会报错
     */
    private void checkRedisCache(Map<SysCacheRefreshTypeEnum, SysCacheRefreshBO> dbTimeMap) {
        // 获取数据库的刷新时间
        if (CollectionUtil.isEmpty(dbTimeMap)) {
            return;
        }

        // 获取redis数据
        Map<SysCacheRefreshTypeEnum, SysCacheRefreshBO> redisTimeMap = getRedisRefreshTime();

        // 对比数据库和redis数据
        dbTimeMap.forEach((typeEnum, dbTime) -> {
            SysCacheRefreshBO sysCacheRefreshBo = redisTimeMap.get(typeEnum);
            // 只有当redis刷新时间早于数据库的时间才刷新数据
            if (sysCacheRefreshBo != null && DateTimeUtil.isBefore(DateTimeUtil.toDateTimeStr(sysCacheRefreshBo.getUpdateTime()),
                    DateTimeUtil.toDateTimeStr(dbTime.getUpdateTime()))) {
                reloadRedisCache(typeEnum);

                setFinishUpdateTime(typeEnum, dbTime);
            }
        });
    }

    /**
     * 检查各项本地缓存是否需要刷新
     * <p>
     * 如果本项目没有声明相应缓存处理类为spring对象，则不会处理
     */
    private void checkLocalCache(Map<SysCacheRefreshTypeEnum, SysCacheRefreshBO> dbTimeMap, Consumer<SysCacheRefreshTypeEnum> consumer) {
        // 获取数据库的刷新时间
        if (CollectionUtil.isEmpty(dbTimeMap)) {
            return;
        }

        // 筛选进行本地缓存的数据类进行刷新
        dbTimeMap.forEach((typeEnum, dbTime) -> {
            LocalDateTime lastUpdateTime = LOCAL_LAST_UPDATE_TIME.get(typeEnum);
            // 只有当本地缓存的刷新时间早于数据库的时间才刷新数据
            if (lastUpdateTime != null && DateTimeUtil.isBefore(DateTimeUtil.toDateTimeStr(lastUpdateTime),
                    DateTimeUtil.toDateTimeStr(dbTime.getUpdateTime()))) {
                reloadLocalCache(typeEnum, consumer);
                LOCAL_LAST_UPDATE_TIME.put(typeEnum, dbTime.getUpdateTime());
            }
        });
    }

    /**
     * 刷新指定缓存和数据库finishUpdateTime，真正刷新缓存
     */
    private void setFinishUpdateTime(SysCacheRefreshTypeEnum typeEnum, SysCacheRefreshBO sysCacheRefreshBo) {
        LocalDateTime now = LocalDateTime.now();

        // 缓存刷新完成时间
        sysCacheRefreshBo.setFinishUpdateTime(now);

        redisHash.put(RedisKeyEnum.SYS_CACHE_REFRESH.getVal(), typeEnum.getVal(), sysCacheRefreshBo.toString());

        // 更新数据库
        sysCacheRefreshService
                .update(
                        new LambdaUpdateWrapper<SysCacheRefresh>()
                                .eq(SysCacheRefresh::getType, typeEnum)
                                .set(SysCacheRefresh::getFinishUpdateTime, now));
    }

    /**
     * 根据指定类型加载缓存
     * <p>
     * 如果本项目没有声明相应缓存处理类为spring对象，则会报错
     *
     * @param typeEnum
     */
    private void reloadRedisCache(SysCacheRefreshTypeEnum typeEnum) {
        BaseCacheHandler cacheHandler = cacheFactory.getCacheHandler(typeEnum);
        if (cacheHandler == null) {
            ApplicationUtil.printError(StrUtil.format("无法刷新缓存[{}]，请显式声明它", typeEnum));
        }

        CacheProperty cacheProperty = cacheHandler.getClass().getAnnotation(CacheProperty.class);
        if (cacheProperty.redis()) {
            LOG.info("检测到[{}]刷新,正在刷新redis缓存", typeEnum);
            cacheHandler.reloadRedis();
            LOG.info("刷新redis缓存[{}]完成", typeEnum);
        }
    }

    /**
     * 根据指定类型加载缓存
     * <p>
     * 如果本项目没有声明相应缓存处理类为spring对象，则不会处理
     *
     * @param typeEnum
     */
    private void reloadLocalCache(SysCacheRefreshTypeEnum typeEnum, Consumer<SysCacheRefreshTypeEnum> consumer) {
        BaseCacheHandler cacheHandler = cacheFactory.getCacheHandler(typeEnum);
        if (cacheHandler == null) {
            return;
        }

        CacheProperty cacheProperty = cacheHandler.getClass().getAnnotation(CacheProperty.class);
        if (cacheProperty.local()) {
            LOG.info("检测到[{}]刷新,正在刷新本地缓存", typeEnum);
            cacheHandler.reloadLocal();

            // consumer
            if (consumer != null) {
                consumer.accept(typeEnum);
            }

            LOG.info("刷新本地缓存[{}]完成", typeEnum);
        }
    }

    /**
     * 初始化所有缓存刷新时间,并缓存
     */
    private void initCacheRefreshTime() {
        // 获取数据库的刷新时间
        getDBRefreshTime(dbTimeMap -> {
            // 如果数据库没有值，则自动填补，自动往数据库加数据
            patchDBRefreshTime(dbTimeMap);

            Map<byte[], byte[]> redisData = new HashMap<>(dbTimeMap.size());
            dbTimeMap.forEach((key, value) -> {
                redisData.put(keySerializer.serialize(key.getVal()), valueSerializer.serialize(value));
                LOCAL_LAST_UPDATE_TIME.put(key, value.getUpdateTime());
            });

            try {
                // 设置到redis，使用pipeline减少删增之间的真空时间
                redisHash.getOperations().executePipelined((RedisCallback<Object>) connection -> {
                    byte[] key = keySerializer.serialize(RedisKeyEnum.SYS_CACHE_REFRESH.getVal());
                    connection.del(key);
                    connection.hMSet(key, redisData);
                    return null;
                });
            } catch (Exception ex) {
                LOG.error(ex);
            }
        });
    }

    /**
     * 如果数据库中没有某些值，则增加到数据库
     *
     * @param dbTimeMap 数据库数据
     */
    private void patchDBRefreshTime(Map<SysCacheRefreshTypeEnum, SysCacheRefreshBO> dbTimeMap) {
        SysCacheRefreshTypeEnum[] typeEnums = SysCacheRefreshTypeEnum.values();
        try {
            for (SysCacheRefreshTypeEnum typeEnum : typeEnums) {

                // 不存在，保存至数据库
                if (!dbTimeMap.containsKey(typeEnum)) {

                    LOG.info("检测到[{}]数据在[sys_cache_refresh]中不存在,自动加入", typeEnum);

                    LocalDateTime now = LocalDateTime.now();

                    SysCacheRefresh sysCacheRefresh = new SysCacheRefresh(typeEnum, typeEnum.getStr());

                    sysCacheRefreshService.save(sysCacheRefresh);
                    dbTimeMap.put(typeEnum, new SysCacheRefreshBO(now, now));
                }
            }
        } catch (Exception ex) {
            LOG.error(ex);
        }
    }

    /**
     * 获取redis的刷新时间，一定不会返回null
     *
     * @return map
     */
    private Map<SysCacheRefreshTypeEnum, SysCacheRefreshBO> getRedisRefreshTime() {
        Map<String, String> entries = redisHash.entries(RedisKeyEnum.SYS_CACHE_REFRESH.getVal());
        if (CollectionUtil.isEmpty(entries)) {
            return new HashMap<>(0);
        }
        try {
            // 转换map
            Map<SysCacheRefreshTypeEnum, SysCacheRefreshBO> timeMap = entries
                    .entrySet()
                    .stream()
                    .filter(entry -> SysCacheRefreshTypeEnum.valueOf(entry.getKey()) != null)
                    .collect(Collectors.toMap(
                            key -> SysCacheRefreshTypeEnum.valueOf(key.getKey()),
                            value -> JSONObject.parseObject(value.getValue(), SysCacheRefreshBO.class)));

            return Optional.of(timeMap).orElseGet(HashMap::new);
        } catch (Exception ex) {
            LOG.error(ex);
            return null;
        }
    }

    /**
     * 获取数据库的刷新时间，一定不会返回null
     *
     * @return map
     */
    private void getDBRefreshTime(Consumer<Map<SysCacheRefreshTypeEnum, SysCacheRefreshBO>> consumer) {
        List<SysCacheRefresh> sysCacheRefreshes = sysCacheRefreshService.list();
        if (CollectionUtil.isEmpty(sysCacheRefreshes)) {
            sysCacheRefreshes = new ArrayList<>();
        }

        // 转换map
        Map<SysCacheRefreshTypeEnum, SysCacheRefreshBO> timeMap =
                CollectionUtil.toMapWithKeyValue(sysCacheRefreshes, v -> v.getType() != null, v -> v.getType(), SysCacheRefreshBO::new);

        Optional.ofNullable(timeMap).ifPresent(consumer);
    }
}
