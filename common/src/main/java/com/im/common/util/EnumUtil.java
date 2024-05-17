package com.im.common.util;

import com.im.common.cache.impl.I18nLanguageCache;
import com.im.common.cache.impl.SysConfigCache;
import com.im.common.entity.enums.*;
import com.im.common.util.i18n.I18nContext;
import com.im.common.util.i18n.I18nTranslateUtil;
import com.im.common.util.spring.SpringContextUtil;

import java.io.Serializable;
import java.util.*;

/**
 * 枚举工具类
 *
 * @author Barry
 * @date 2019-10-12
 */
public final class EnumUtil {
    /**
     * 数据字典<语言编码, <枚举名, <值, 翻译(排序按原顺序)>>>，全部枚举
     */
    private static Map<String, Map<String, Map<String, String>>> ENUM_MAP_ALL = new HashMap<>();
    /**
     * 数据字典<语言编码, <枚举名, <值, 翻译(排序按原顺序)>>>，给前台用的，数据没那么多
     */
    private static Map<String, Map<String, Map<String, String>>> ENUM_MAP_PORTAL = new HashMap<>();
    private static Set<String> ENUM_SET_PORTAL = new HashSet<>();

    static {
        ENUM_SET_PORTAL.add(SexEnum.class.getSimpleName());

        ENUM_SET_PORTAL.add(UserBillTypeEnum.class.getSimpleName());

        ENUM_SET_PORTAL.add(RechargeConfigGroupEnum.class.getSimpleName());
        ENUM_SET_PORTAL.add(RechargeConfigSourceEnum.class.getSimpleName());
        ENUM_SET_PORTAL.add(RechargeOrderStatusEnum.class.getSimpleName());

        ENUM_SET_PORTAL.add(WithdrawConfigGroupEnum.class.getSimpleName());
        ENUM_SET_PORTAL.add(WithdrawConfigSourceEnum.class.getSimpleName());
        ENUM_SET_PORTAL.add(WithdrawOrderStatusEnum.class.getSimpleName());
    }

    private static SysConfigCache sysConfigCache;

    public static void main(String[] args) {
        // Set<String> set = new LinkedHashSet<>();
        // set.add(LotteryBetModeEnum.YUAN2.getVal());
        // set.add(LotteryBetModeEnum.YUAN1.getVal());
        // set.add(LotteryBetModeEnum.JIAO2.getVal());
        // set.add(LotteryBetModeEnum.JIAO1.getVal());
        //
        // System.out.println(CollectionUtil.join(set, ","));
    }

    /**
     * 初始化所有ENUM字典到内存中
     */
    public static void initEnumMap(I18nLanguageCache i18nLanguageCache) {
        String enumPackage = System.getProperty("mybatis-plus.type-enums-package");
        Set<Class<?>> allEnumClass = ClassUtil.scanPackageBySuper(enumPackage, BaseEnum.class);

        Map<String, BaseEnum[]> allEnumConstantMap = new HashMap<>(allEnumClass.size());
        for (Class<?> enumClass : allEnumClass) {
            BaseEnum[] enumConstants = ((Class<BaseEnum>) enumClass).getEnumConstants();
            allEnumConstantMap.put(enumClass.getSimpleName(), enumConstants);
        }

        List<String> languageList = i18nLanguageCache.listLanguageCodeFromLocal();

        for (String language : languageList) {
            // <枚举名, <值, 翻译>>
            Map<String, Map<String, String>> languageEnumMapAll = new HashMap<>(allEnumConstantMap.size());
            // <枚举名, <值, 翻译>>
            Map<String, Map<String, String>> languageEnumMapPortal = new HashMap<>(ENUM_SET_PORTAL.size());

            Set<Map.Entry<String, BaseEnum[]>> enumEntries = allEnumConstantMap.entrySet();
            for (Map.Entry<String, BaseEnum[]> enumEntry : enumEntries) {
                String enumName = enumEntry.getKey();
                BaseEnum[] enumConstants = enumEntry.getValue();

                Map<String, String> singleEnumMap = new LinkedHashMap<>(enumConstants.length);

                for (BaseEnum e : enumConstants) {
                    singleEnumMap.put(e.getVal(), I18nTranslateUtil.translate(language, e.getStr()));
                }

                languageEnumMapAll.put(enumName, singleEnumMap);

                if (ENUM_SET_PORTAL.contains(enumName)) {
                    languageEnumMapPortal.put(enumName, singleEnumMap);
                }
            }

            ENUM_MAP_ALL.put(language, languageEnumMapAll);
            ENUM_MAP_PORTAL.put(language, languageEnumMapPortal);
        }
    }

    /**
     * 全部枚举
     *
     * @return
     */
    public static Map<String, Map<String, String>> getCurrentLanguageEnumMap() {
        String language = I18nContext.getLanguage();
        if (StrUtil.isBlank(language)) {
            language = getSysConfigCache().getGlobalConfigFromLocal().getDefaultI18n();
        }
        return ENUM_MAP_ALL.get(language);
    }

    /**
     * 全部枚举
     *
     * @return
     */
    public static Map<String, Map<String, String>> getCurrentLanguageEnumMapForPortal() {
        String language = I18nContext.getLanguage();
        if (StrUtil.isBlank(language)) {
            language = getSysConfigCache().getGlobalConfigFromLocal().getDefaultI18n();
        }
        return ENUM_MAP_PORTAL.get(language);
    }

    /**
     * 转换枚举
     *
     * @param enumClass 枚举类
     * @param value     枚举值
     * @param <T>       类型
     * @return BaseEnum
     */
    public static <T extends BaseEnum> T valueOfIEnum(Class<? extends BaseEnum> enumClass, Serializable value) {
        BaseEnum[] enumConstants = enumClass.getEnumConstants();

        for (BaseEnum enumConstant : enumConstants) {
            if (enumConstant.getValue().equals(value)) {
                return (T) enumConstant;
            }
        }

        return null;
    }

    private static SysConfigCache getSysConfigCache() {
        if (sysConfigCache == null) {
            sysConfigCache = SpringContextUtil.getBean(SysConfigCache.class);
        }
        return sysConfigCache;
    }
}
