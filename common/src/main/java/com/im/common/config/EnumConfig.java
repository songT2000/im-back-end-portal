package com.im.common.config;

import com.im.common.cache.impl.I18nLanguageCache;
import com.im.common.cache.impl.I18nTranslateCache;
import com.im.common.util.EnumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

/**
 * @author Barry
 * @date 2020-06-11
 */
@Component
@ConditionalOnBean(I18nLanguageCache.class)
public class EnumConfig {

    @Autowired
    public EnumConfig(I18nLanguageCache i18nLanguageCache, I18nTranslateCache i18nTranslateCache) {
        EnumUtil.initEnumMap(i18nLanguageCache);
    }
}
