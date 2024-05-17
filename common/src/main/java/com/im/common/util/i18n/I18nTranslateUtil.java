package com.im.common.util.i18n;

import cn.hutool.core.util.StrUtil;
import com.im.common.cache.impl.SysConfigCache;
import com.im.common.cache.sysconfig.bo.GlobalConfigBO;
import com.im.common.constant.CommonConstant;
import com.im.common.entity.I18nTranslate;
import com.im.common.util.CollectionUtil;
import com.im.common.util.spring.SpringContextUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 翻译工具类
 *
 * @author Barry
 * @date 2019-11-20
 */
public class I18nTranslateUtil {
    private static final int NEED_I18N_SUFFIX_LENGTH = CommonConstant.NEED_I18N_SUFFIX.length();

    private static SysConfigCache sysConfigCache;

    private I18nTranslateUtil() {
    }

    /**
     * 翻译表<language_group.key, value>，数据库i18n_translate表里的数据，系统初始化时传入
     */
    private static Map<String, String> TRANSLATE_MAP = new HashMap<>();

    /**
     * 设置翻译列表
     *
     * @param translateList 翻译列表，数据库数据
     */
    public static void setTranslateMap(List<I18nTranslate> translateList) {
        Map<String, String> translateMap = new HashMap<>(16);

        if (CollectionUtil.isNotEmpty(translateList)) {
            translateMap = CollectionUtil
                    .toMapWithKeyValue(translateList,
                            translate -> translate.getLanguageCode() + "_" + translate.getGroup() + CommonConstant.POINT_EN + translate.getKey(),
                            translate -> translate.getValue());
        }

        TRANSLATE_MAP = translateMap;
    }

    /**
     * 翻译为当前语言，必须是合法结尾才会尝试翻译，翻译失败原样返回，超高性能
     *
     * @param language 语言编码
     * @param key      翻译key
     * @return String
     */
    public static String translate(String language, String key) {
        if (StrUtil.isBlank(language)) {
            return key;
        }
        if (!isI18nKey(key)) {
            return key;
        }

        return tryTranslate(language, key);
    }

    /**
     * 翻译为当前语言，必须是合法结尾才会尝试翻译，翻译失败原样返回，超高性能
     * 该方法只能在正常的request请求时的同一线程使用
     * 必须保证setTranslateMap被先调用
     *
     * @param key 翻译key
     * @return String
     */
    public static String translate(String key) {
        if (!isI18nKey(key)) {
            return key;
        }

        // 获取当前语言
        String language = I18nContext.getLanguage();
        if (StrUtil.isBlank(language)) {
            SysConfigCache sysConfigCache = getSysConfigCache();
            if (sysConfigCache != null) {
                GlobalConfigBO globalConfig = sysConfigCache.getGlobalConfigFromLocal();
                language = globalConfig.getDefaultI18n();
            }
        }
        if (StrUtil.isBlank(language)) {
            return key;
        }

        return tryTranslate(language, key);
    }

    public static boolean isI18nKey(String key) {
        return StrUtil.isNotBlank(key) && key.endsWith(CommonConstant.NEED_I18N_SUFFIX);
    }

    private static String tryTranslate(String language, String key) {
        String translateKey = extraTranslateKey(key);
        String mapKey = language + "_" + translateKey;
        if (TRANSLATE_MAP.containsKey(mapKey)) {
            return TRANSLATE_MAP.get(mapKey);
        }
        return key;
    }

    private static String extraTranslateKey(String key) {
        return key.substring(0, key.length() - NEED_I18N_SUFFIX_LENGTH);
    }

    private static SysConfigCache getSysConfigCache() {
        if (sysConfigCache == null) {
            sysConfigCache = SpringContextUtil.getBean(SysConfigCache.class);
        }
        return sysConfigCache;
    }
}
