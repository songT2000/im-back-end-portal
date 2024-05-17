package com.im.common.cache.impl;

import com.im.common.cache.base.BaseCacheHandler;
import com.im.common.cache.base.CacheProperty;
import com.im.common.entity.SmsTemplateConfig;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.service.SmsTemplateConfigService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 短信模板配置
 *
 * @author Barry
 * @date 2022-02-10
 */
@CacheProperty(value = SysCacheRefreshTypeEnum.SMS_TEMPLATE_CONFIG, redis = false, local = true)
@Component
public class SmsTemplateConfigCache implements BaseCacheHandler {
    private SmsTemplateConfigService smsTemplateConfigService;

    /**
     * 本地缓存
     */
    private List<SmsTemplateConfig> LOCAL_LIST = new ArrayList<>();
    private Map<String, SmsTemplateConfig> LOCAL_CODE_CACHE = new HashMap<>();

    @Autowired
    public void setSmsTemplateConfigService(SmsTemplateConfigService smsTemplateConfigService) {
        this.smsTemplateConfigService = smsTemplateConfigService;
    }

    @Override
    public void reloadLocal() {
        loadAndCache(this::resolveListForLocal);
    }

    /**
     * 查询所有列表，包含启用&禁用的
     *
     * @return List<SmsTemplateConfig>
     */
    public List<SmsTemplateConfig> listFromLocal() {
        return LOCAL_LIST;
    }

    /**
     * 根据国家编码查找
     *
     * @param code 国家编码
     * @return SmsTemplateConfig
     */
    public SmsTemplateConfig getByCodeFromLocal(String code) {
        if (StrUtil.isBlank(code)) {
            return null;
        }
        return LOCAL_CODE_CACHE.get(code);
    }

    /**
     * 加载并缓存数据
     *
     * @param consumer 处理方法
     */
    private void loadAndCache(Consumer<List<SmsTemplateConfig>> consumer) {
        List<SmsTemplateConfig> list = smsTemplateConfigService.list();

        consumer.accept(list);
    }

    /**
     * 解析并缓存数据
     *
     * @param list 银行列表
     */
    private void resolveListForLocal(List<SmsTemplateConfig> list) {
        // 分组
        Map<String, SmsTemplateConfig> codeMap = CollectionUtil.toMapWithKey(list, e -> e.getCode());

        LOCAL_LIST = list;
        LOCAL_CODE_CACHE = codeMap;
    }
}
