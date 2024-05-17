package com.im.common.cache.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.im.common.cache.base.BaseCacheHandler;
import com.im.common.cache.base.CacheProperty;
import com.im.common.constant.RedisKeyEnum;
import com.im.common.entity.I18nLanguage;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.service.I18nLanguageService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.redis.RedisKeySerializer;
import com.im.common.util.redis.RedisValueSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>国际化语言类型配置</p>
 *
 * @author Barry
 * @date 2019/10/25
 */
@CacheProperty(value = SysCacheRefreshTypeEnum.I18N_LANGUAGE, redis = true, local = true)
@Component
public class I18nLanguageCache implements BaseCacheHandler {
    private I18nLanguageService i18nLanguageService;
    private HashOperations<String, String, String> redisHash;
    private RedisKeySerializer keySerializer;
    private RedisValueSerializer valueSerializer;

    private List<String> LANGUAGE_LIST = new ArrayList<>();
    private Map<String, String> LOCAL_CODE_NAME_CACHE = new HashMap<>();

    @Autowired
    public void setI18nLanguageService(I18nLanguageService i18nLanguageService) {
        this.i18nLanguageService = i18nLanguageService;
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

    public List<String> listLanguageCodeFromLocal() {
        return LANGUAGE_LIST;
    }

    public List<I18nLanguage> listEnabledFromRedis() {
        return CollectionUtil.filterList(listFromRedis(), language -> Boolean.TRUE.equals(language.getEnabled()));
    }

    public List<I18nLanguage> listFromRedis() {
        Map<String, String> entries = redisHash.entries(RedisKeyEnum.I18N_LANGUAGE_BY_CODE.getVal());
        if (CollectionUtil.isEmpty(entries)) {
            return new ArrayList<>();
        }

        Collection<String> valuesStrList = entries.values();

        List<I18nLanguage> i18nLanguages = CollectionUtil.toList(valuesStrList, s -> JSON.parseObject(s, I18nLanguage.class));
        return CollectionUtil.sort(i18nLanguages, Comparator.comparingInt(I18nLanguage::getSort));
    }

    public I18nLanguage getByCode(String code) {
        String str = redisHash.get(RedisKeyEnum.I18N_LANGUAGE_BY_CODE.getVal(), code);
        if (StrUtil.isBlank(str)) {
            return null;
        }
        return JSON.parseObject(str, I18nLanguage.class);
    }

    public String getNameByCodeFromLocal(String code) {
        return LOCAL_CODE_NAME_CACHE.get(code);
    }

    /**
     * 加载并缓存数据
     */
    private void loadAndCache() {
        List<I18nLanguage> list = i18nLanguageService.list();

        LANGUAGE_LIST = CollectionUtil.toList(list, e -> e.getCode());

        LOCAL_CODE_NAME_CACHE = CollectionUtil.toMapWithKeyValue(list, e -> e.getCode(), e -> e.getName());

        Optional.ofNullable(list).ifPresent(this::resolveList);
    }

    /**
     * 解析并缓存数据
     *
     * @param list 彩票列表
     */
    private void resolveList(List<I18nLanguage> list) {
        // 分组
        Map<String, I18nLanguage> codeMap = CollectionUtil.toMapWithKey(list, I18nLanguage::getCode);

        // 分组处理数据
        if (CollectionUtil.isNotEmpty(codeMap)) {
            this.resolveCodeList(codeMap);
        }
    }

    private void resolveCodeList(Map<String, I18nLanguage> idMap) {
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
                byte[] key = keySerializer.serialize(RedisKeyEnum.I18N_LANGUAGE_BY_CODE.getVal());
                connection.del(key);
                if (CollectionUtil.isNotEmpty(redisData)) {
                    connection.hMSet(key, redisData);
                }
            }

            return null;
        });
    }
}
