package com.im.common.util.i18n;

/**
 * 存储当前用户是哪个语言
 *
 * @author Barry
 */
public class I18nContext {

    /**
     * 当前语言编码
     */
    private final static ThreadLocal<String> LANGUAGE = new ThreadLocal<>();

    /**
     * 设置当前语言编码，同一线程内有效
     *
     * @param language 语言编码
     */
    public static void setLanguage(String language) {
        LANGUAGE.set(language);
    }

    /**
     * 获取当前语言编码，同一线程内有效
     *
     * @return 当前语言编码
     */
    public static String getLanguage() {
        return LANGUAGE.get();
    }

    /**
     * 当前上下文是否具有值
     *
     * @return boolean
     */
    public static boolean hasLanguage() {
        return LANGUAGE.get() != null;
    }
}
