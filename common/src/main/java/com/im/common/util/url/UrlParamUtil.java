package com.im.common.util.url;

import cn.hutool.core.util.StrUtil;
import com.im.common.constant.CommonConstant;
import com.im.common.util.CollectionUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * url参数工具类
 *
 * @author Barry
 * @date 2019/2/12
 */
public class UrlParamUtil {
    private static final String EMPTY = "";
    private static final String DEFAULT_EQUALS = "=";
    private static final String DEFAULT_SEPARATOR = "&";

    /**
     * 将数组转换为URL格式：key1=value1&key2=value2，忽略null和空字符串
     * <p>
     * key会按照ascii排序
     *
     * @param params 参数，单位为key，双位为value
     * @return URL
     */
    public static String arrayJoin(Object... params) {
        if (params == null) {
            return EMPTY;
        }

        if (params.length % CommonConstant.INT_2 != CommonConstant.INT_0) {
            throw new IllegalArgumentException("RSP_MSG.SYS_REQUEST_PARAM_ERROR#I18N");
        }

        Map<String, String> mapParams = new TreeMap<>();
        for (int i = 0; i < params.length; i += CommonConstant.INT_2) {
            Object value = params[i + 1];
            mapParams.put(params[i].toString(), value == null ? null : value.toString());
        }

        return join(mapParams);
    }

    /**
     * 将Map中的参数转换为URL格式：key1=value1&key2=value2，忽略null和空字符串
     *
     * @param params 参数
     * @return URL
     */
    public static String join(Map<String, String> params) {
        return join(params, DEFAULT_SEPARATOR, DEFAULT_EQUALS, true);
    }

    /**
     * 将Map中的参数转换为URL格式：key1=value1&key2=value2，忽略null和空字符串
     *
     * @param params      参数
     * @param ignoreEmpty 是否忽略null和空字符串
     * @return URL
     */
    public static String join(Map<String, String> params, boolean ignoreEmpty) {
        return join(params, DEFAULT_SEPARATOR, DEFAULT_EQUALS, ignoreEmpty);
    }

    /**
     * 将Map中的参数转换为URL格式：key1=value1{separator}key2=value2，忽略null和空字符串
     *
     * @param params    参数
     * @param separator 每组参数之间的分割符，如&
     * @return URL
     */
    public static String join(Map<String, String> params, String separator) {
        return join(params, separator, DEFAULT_EQUALS, true);
    }

    /**
     * 将Map中的参数转换为URL格式：key1=value1&key2=value2
     *
     * @param params      参数
     * @param ignoreEmpty 是否忽略null和空字符串
     * @return URL
     */
    public static String contact(Map<String, String> params, boolean ignoreEmpty) {
        return join(params, DEFAULT_SEPARATOR, DEFAULT_EQUALS, ignoreEmpty);
    }

    /**
     * 将Map中的参数转换为URL格式：key1=value1&key2=value2
     *
     * @param params      参数
     * @param separator   每组参数之间的分割符，如&
     * @param ignoreEmpty 是否忽略null和空字符串
     * @return URL
     */
    public static String join(Map<String, String> params, String separator, boolean ignoreEmpty) {
        return join(params, separator, DEFAULT_EQUALS, ignoreEmpty);
    }

    /**
     * 将Map中的参数转换为URL格式：key1=value1&key2=value2
     *
     * @param params      参数
     * @param separator   每大组参数之间的分割符，如&
     * @param equals      每小组参数之间的分割符，如=
     * @param ignoreEmpty 是否忽略null和空字符串
     * @return URL
     */
    public static String join(Map<String, String> params, String separator, String equals, boolean ignoreEmpty) {
        if (params == null || params.isEmpty()) {
            return EMPTY;
        }

        StringBuffer url = new StringBuffer();

        Iterator<Entry<String, String>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String> entry = it.next();
            String value = entry.getValue();
            // 主动忽略空值并且值为空
            if (ignoreEmpty && StrUtil.isBlank(value)) {
                continue;
            }

            url.append(entry.getKey()).append(equals).append(value);
            if (it.hasNext()) {
                url.append(separator);
            }
        }

        String urlStr = url.toString();
        if (urlStr.endsWith(separator)) {
            urlStr = urlStr.substring(0, urlStr.length() - separator.length());
        }

        return urlStr;
    }

    /**
     * 将一串CURL格式的字符串转成HashMap
     *
     * @param url CURL格式的字符串，如a=1&b=2&c=3
     * @return HashMap
     */
    public static Map<String, String> fromUrl(String url) {
        Map<String, String> paramsMap = new HashMap<>(16);

        String[] params = url.split(DEFAULT_SEPARATOR);
        for (String param : params) {
            String[] values = param.split(DEFAULT_EQUALS);

            if (values == null || values.length <= 1) {
                continue;
            }

            String key = values.length > 0 ? values[0] : null;
            String value = values.length > 1 ? values[1] : null;
            if (key == null) {
                continue;
            }

            paramsMap.put(key, value);
        }

        return paramsMap;
    }

    /**
     * 将Get请求的url与参数进行拼接，如http://www.baidu.com，封装成http://www.baidu.com?a=1&b=2
     * 忽略参数的null和空字符串
     *
     * @param url    一般给域名就好
     * @param params 请求参数列表
     * @return
     */
    public static String concatGetUrl(String url, Map<String, String> params) {
        String requestUrl = url;

        if (CollectionUtil.isNotEmpty(params)) {
            String paramUrl = UrlParamUtil.join(params, true);
            if (requestUrl.indexOf(CommonConstant.QUESTION_EN) > -1) {
                if (requestUrl.indexOf(CommonConstant.EQUAL) > -1) {
                    requestUrl = requestUrl + "&" + paramUrl;
                } else {
                    requestUrl += paramUrl;
                }
            } else {
                requestUrl = requestUrl + "?" + paramUrl;
            }
        }

        return requestUrl;
    }

    /**
     * 将Get请求的url与参数进行拼接，如http://www.baidu.com，封装成http://www.baidu.com?a=1&b=2
     *
     * @param url         一般给域名就好
     * @param params      请求参数列表
     * @param ignoreEmpty 是否忽略参数的null和空字符串
     * @return
     */
    public static String concatGetUrl(String url, Map<String, String> params, boolean ignoreEmpty) {
        String requestUrl = url;

        if (CollectionUtil.isNotEmpty(params)) {
            String paramUrl = UrlParamUtil.join(params, ignoreEmpty);
            if (requestUrl.indexOf(CommonConstant.QUESTION_EN) > -1) {
                requestUrl += paramUrl;
            } else {
                requestUrl = requestUrl + "?" + paramUrl;
            }
        }

        return requestUrl;
    }
}
