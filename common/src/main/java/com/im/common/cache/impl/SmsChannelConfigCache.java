package com.im.common.cache.impl;

import com.im.common.cache.base.BaseCacheHandler;
import com.im.common.cache.base.CacheProperty;
import com.im.common.entity.SmsChannelConfig;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.service.SmsChannelConfigService;
import com.im.common.util.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 短信信令通道配置
 *
 * @author Barry
 * @date 2022-02-10
 */
@CacheProperty(value = SysCacheRefreshTypeEnum.SMS_CHANNEL_CONFIG, redis = false, local = true)
@Component
public class SmsChannelConfigCache implements BaseCacheHandler {
    private SmsChannelConfigService smsChannelConfigService;

    /**
     * 本地缓存
     */
    private Map<String, SmsChannelConfig> LOCAL_CODE_CACHE = new HashMap<>();

    @Autowired
    public void setSmsChannelConfigService(SmsChannelConfigService smsChannelConfigService) {
        this.smsChannelConfigService = smsChannelConfigService;
    }

    @Override
    public void reloadLocal() {
        loadAndCache(this::resolveListForLocal);
    }

    /**
     * 根据编码查找
     *
     * @param code 编码
     * @return SmsChannelConfig
     */
    public SmsChannelConfig getByCodeFromLocal(String code) {
        return LOCAL_CODE_CACHE.get(code);
    }

    /**
     * 加载并缓存数据
     *
     * @param consumer 处理方法
     */
    private void loadAndCache(Consumer<List<SmsChannelConfig>> consumer) {
        List<SmsChannelConfig> list = smsChannelConfigService.list();

        consumer.accept(list);
    }

    /**
     * 解析并缓存数据
     *
     * @param list 银行列表
     */
    private void resolveListForLocal(List<SmsChannelConfig> list) {
        // 分组
        Map<String, SmsChannelConfig> codeMap = CollectionUtil.toMapWithKey(list, e -> e.getCode());

        LOCAL_CODE_CACHE = codeMap;
    }
}
