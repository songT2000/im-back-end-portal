package com.im.common.cache.impl;

import com.im.common.cache.base.BaseCacheHandler;
import com.im.common.cache.base.CacheProperty;
import com.im.common.entity.I18nTranslate;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.service.I18nTranslateService;
import com.im.common.util.i18n.I18nTranslateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * <p>翻译配置</p>
 *
 * @author Barry
 * @date 2019/10/25
 */
@CacheProperty(value = SysCacheRefreshTypeEnum.I18N_TRANSLATE, redis = false, local = true)
@Component
public class I18nTranslateCache implements BaseCacheHandler {
    private I18nTranslateService i18nTranslateService;

    @Autowired
    public void setI18nTranslateService(I18nTranslateService i18nTranslateService) {
        this.i18nTranslateService = i18nTranslateService;
    }

    @Override
    public void reloadLocal() {
        loadAndCache();
    }

    /**
     * 加载并缓存数据
     */
    private void loadAndCache() {
        List<I18nTranslate> list = i18nTranslateService.list();

        Optional.ofNullable(list).ifPresent(I18nTranslateUtil::setTranslateMap);
    }
}
