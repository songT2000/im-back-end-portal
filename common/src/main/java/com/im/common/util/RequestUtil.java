package com.im.common.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.im.common.constant.CommonConstant;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet请求工具类
 *
 * @author Barry
 * @date 2018/5/17
 */
public final class RequestUtil {
    private RequestUtil() {
    }

    private static final Log LOG = LogFactory.get();


    /**
     * 非法的IP地址
     **/
    private static final String UNKNOWN_IP = "unknown";

    /**
     * 多个IP地址的分割符
     **/
    private static final String MULTIPLE_IP_SEPARATOR = ",";

    /**
     * 本机地址的另外一种表示方式
     **/
    private static final String IP4_LOCALHOST_LONG = "0:0:0:0:0:0:0:1";

    /**
     * 本机地址
     **/
    private static final String IP4_LOCALHOST = "127.0.0.1";

    /**
     * http 80端口
     **/
    private static final int HTTP_PORT = 80;

    /**
     * https 443端口
     **/
    private static final int HTTPS_PORT = 443;


    /**
     * http协议
     **/
    private static final String HTTP_SCHEME_START = "http";

    /**
     * https协议
     **/
    private static final String HTTPS_SCHEME_START = "https";

    /**
     * 协议的后半部分字符串
     **/
    private static final String HTTP_SCHEME_SUFFIX = "://";

    /**
     * http协议
     **/
    private static final String HTTP_SCHEME_FULL = HTTP_SCHEME_START + HTTP_SCHEME_SUFFIX;

    /**
     * https协议
     **/
    private static final String HTTPS_SCHEME_FULL = HTTPS_SCHEME_START + HTTP_SCHEME_SUFFIX;

    /**
     * HTTP头 Host
     **/
    private static final String HTTP_HEADER_HOST = "Host";

    /**
     * HTTP头 User-Agent
     **/
    private static final String HTTP_HEADER_USER_AGENT = "User-Agent";

    /**
     * HTTP头 referer
     **/
    private static final String HTTP_HEADER_REFERER = "referer";

    /**
     * HTTP头 X-Forwarded-For
     **/
    private static final String HTTP_HEADER_X_FORWARDED_FOR = "X-Forwarded-For";

    /**
     * HTTP头 X-Forwarded-Proto
     **/
    private static final String HTTP_HEADER_HTTP_X_FORWARDED_PROTO = "X-Forwarded-Proto";

    /**
     * HTTP头 Proxy-Client-IP
     **/
    private static final String HTTP_HEADER_PROXY_CLIENT_IP = "Proxy-Client-IP";

    /**
     * HTTP头 WL-Proxy-Client-IP
     **/
    private static final String HTTP_HEADER_WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";

    /**
     * HTTP头 HTTP_CLIENT_IP
     **/
    private static final String HTTP_HEADER_HTTP_CLIENT_IP = "HTTP_CLIENT_IP";

    /**
     * HTTP头 HTTP_X_FORWARDED_FOR
     **/
    private static final String HTTP_HEADER_HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";

    /**
     * HTTP头 x-requested-with
     **/
    private static final String HTTP_HEADER_X_REQUESTED_WITH = "x-requested-with";

    /**
     * HTTP头 x-requested-with的值XMLHttpRequest
     **/
    private static final String HTTP_HEADER_X_REQUESTED_WITH_AJAX_VALUE = "XMLHttpRequest";


    /**
     * 获取用户访问IP，如有使用CDN等，必须在CDN那边处理用户真实IP，不能让人注入X-FORWARDED-FOR，不要直接修改这里的代码
     * 有可能返回ipv4也有可能返回ipv6，请注意区分
     *
     * @param request http request 请求对象
     * @return IP ipv4|ipv6
     */
    public static String getRequestIp(HttpServletRequest request) {
        String ip = getHeader(request, HTTP_HEADER_X_FORWARDED_FOR);
        if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = getHeader(request, HTTP_HEADER_PROXY_CLIENT_IP);
        }
        if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = getHeader(request, HTTP_HEADER_WL_PROXY_CLIENT_IP);
        }
        if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = getHeader(request, HTTP_HEADER_HTTP_X_FORWARDED_FOR);
        }
        if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.split(MULTIPLE_IP_SEPARATOR).length > 0) {
            ip = ip.split(MULTIPLE_IP_SEPARATOR)[0];
        }

        if (IP4_LOCALHOST_LONG.equals(ip)) {
            return IP4_LOCALHOST;
        }

        return ip;
    }

    /**
     * 获取用户当前访问的域名地址,返回http(s)://xxx.xxx.com
     *
     * @param request request请求
     * @return 当前访问的域名地址
     */
    public static String getRequestWebsite(HttpServletRequest request) {
        String website = getScheme(request) + HTTP_SCHEME_SUFFIX + getHost(request);

        Integer port = getPort(request);
        if (port != null && port != HTTP_PORT && port != HTTPS_PORT) {
            website = website + ":" + port;
        }

        return website;
    }

    /**
     * 获取用户当前访问的refer
     *
     * @param request request请求
     * @return refer
     */
    public static String getReferer(HttpServletRequest request) {
        return getHeader(request, HTTP_HEADER_REFERER);
    }

    /**
     * 获取访问协议，如协议获取不正确,尝试在nginx中配置  proxy_set_header   X-Forwarded-Proto $scheme;
     * <p>
     * 优先获取http头中的referer
     *
     * @param request 请求对象
     * @return http|https
     */
    public static String getScheme(HttpServletRequest request) {
        String scheme = null;

        // 灰度的
        {
            String gpProtocol = getHeader(request, "gp-protocol");
            if (StrUtil.isNotBlank(gpProtocol)) {
                if (HTTPS_SCHEME_START.equalsIgnoreCase(gpProtocol)) {
                    scheme = HTTPS_SCHEME_START;
                } else if (HTTP_SCHEME_START.equalsIgnoreCase(gpProtocol)) {
                    scheme = HTTP_SCHEME_START;
                }
            }
            if (StrUtil.isNotBlank(scheme)) {
                return scheme;
            }
        }

        // 通用的origin
        {
            String origin = getHeader(request, "origin");
            if (StrUtil.isNotBlank(origin)) {
                if (origin.startsWith(HTTPS_SCHEME_FULL)) {
                    scheme = HTTPS_SCHEME_START;
                } else if (origin.startsWith(HTTP_SCHEME_FULL)) {
                    scheme = HTTP_SCHEME_START;
                }
            }
            if (StrUtil.isNotBlank(scheme)) {
                return scheme;
            }
        }

        // 通用的referer
        {
            String referer = getHeader(request, HTTP_HEADER_REFERER);
            if (StrUtil.isNotBlank(referer)) {
                if (referer.startsWith(HTTPS_SCHEME_FULL)) {
                    scheme = HTTPS_SCHEME_START;
                } else if (referer.startsWith(HTTP_SCHEME_FULL)) {
                    scheme = HTTP_SCHEME_START;
                }
            }
            if (StrUtil.isNotBlank(scheme)) {
                return scheme;
            }
        }

        // 通用的x-forwarded-proto
        {
            String proto = getHeader(request, HTTP_HEADER_HTTP_X_FORWARDED_PROTO);
            if (StrUtil.isNotBlank(proto)) {
                if (HTTPS_SCHEME_START.equalsIgnoreCase(proto)) {
                    scheme = HTTPS_SCHEME_START;
                } else if (HTTP_SCHEME_START.equalsIgnoreCase(proto)) {
                    scheme = HTTP_SCHEME_START;
                }
            }
            if (StrUtil.isNotBlank(scheme)) {
                return scheme;
            }
        }

        return request.getScheme();
    }

    /**
     * 获取http头的host，一般返回当前访问域名xxx.xxx.com
     *
     * @param request 请求对象
     * @return host头
     */
    public static String getHost(HttpServletRequest request) {
        return getHeader(request, HTTP_HEADER_HOST);
    }

    /**
     * 获取http头的port，返回80/443等
     *
     * @param request
     * @return
     */
    public static Integer getPort(HttpServletRequest request) {
        // 灰度的
        {
            String gpPort = getHeader(request, "gp-port");
            if (StrUtil.isNotBlank(gpPort) && NumberUtil.isInteger(gpPort)) {
                return Convert.toInt(gpPort);
            }
        }

        // 通用的origin
        {
            String origin = getHeader(request, "origin");
            if (StrUtil.isNotBlank(origin)) {
                try {
                    URL url = URLUtil.url(origin);
                    if (url != null && url.getPort() != -1) {
                        return url.getPort();
                    }
                } catch (Exception e) {
                }
            }
        }

        // 通用的referer
        {
            String referer = getHeader(request, HTTP_HEADER_REFERER);
            if (StrUtil.isNotBlank(referer)) {
                try {
                    URL url = URLUtil.url(referer);
                    if (url != null && url.getPort() != -1) {
                        return url.getPort();
                    }
                } catch (Exception e) {
                }
            }
        }

        return request.getServerPort();
    }

    /**
     * 获取用户访问的浏览器标识，http头中User-Agent
     *
     * @param request 请求对象
     * @return 浏览器标识
     */
    public static String getUserAgent(HttpServletRequest request) {
        return getHeader(request, HTTP_HEADER_USER_AGENT);
    }

    /**
     * 返回经过xss过滤的http头，如果没有相应头，则返回空字符串，不是NULL，是空字符串
     *
     * @param request 请求对象
     * @param header  http头标识
     * @return http头对应值
     */
    public static String getHeader(HttpServletRequest request, String header) {
        String headerValue = request.getHeader(header);
        if (headerValue == null) {
            return StrUtil.EMPTY;
        }
        headerValue = XssUtil.clean(headerValue.trim());
        return headerValue;
    }

    /**
     * 把所有请求的参数封装为map，并将stream封装为reqBody
     *
     * @param request http请求
     * @return HashMap
     */
    public static Map<String, String> getAllRequestParams(HttpServletRequest request) {
        try {
            Map<String, String> param = new HashMap<>(16);
            Map<String, String[]> requestParameterMap = request.getParameterMap();
            requestParameterMap.entrySet().stream().forEach(entry -> param.put(entry.getKey(), entry.getValue()[0]));

            // 获取stream中的内容
            String reqBody = HttpUtil.getString(request.getInputStream(), Charset.forName("UTF-8"), true);
            if (StrUtil.isNotBlank(reqBody)) {
                param.put("reqBody", reqBody);
            }

            return param;
        } catch (IOException e) {
            LOG.error(e, "发生异常");
        }

        return new HashMap<>(16);
    }

    /**
     * 获取http请求Integer参数，没值或值类型错误则抛出异常{@link IllegalArgumentException}
     *
     * @param request 请求对象
     * @param name    参数名称
     * @return 参数值
     */
    public static Integer getIntegerParamNotNull(HttpServletRequest request, String name, String msg) {
        Integer value = getIntegerParam(request, name, null);
        return Assert.notNull(value, msg);
    }

    /**
     * 获取http请求Integer参数，没值或值类型错误则返回null，不会报错
     *
     * @param request 请求对象
     * @param name    参数名称
     * @return 参数值
     */
    public static Integer getIntegerParam(HttpServletRequest request, String name) {
        return getIntegerParam(request, name, null);
    }

    /**
     * 获取http请求Integer参数，没值或值类型错误则返回默认值，不会报错
     *
     * @param request 请求对象
     * @param name    参数名称
     * @param def     转换错误时的默认值
     * @return 参数值
     */
    public static Integer getIntegerParam(HttpServletRequest request, String name, Integer def) {
        String value = request.getParameter(name);
        try {
            return Convert.toInt(value, def);
        } catch (Exception e) {
        }
        return def;
    }

    /**
     * 获取http请求String参数，并调用{@link String#trim() String.trim()}方法，null或字符串空（全空格也算字符串空）值则抛出异常{@link IllegalArgumentException}
     *
     * @param request 请求对象
     * @param name    参数名称
     * @return 参数值
     */
    public static String getStringParamTrimNotBlank(HttpServletRequest request, String name, String msg) {
        String value = getStringParamTrim(request, name, null);
        return Assert.notBlank(value, msg);
    }

    /**
     * 获取http请求String参数，并调用{@link String#trim() String.trim()}方法，null或字符串空（全空格也算字符串空）值则返回null，不会报错
     *
     * @param request 请求对象
     * @param name    参数名称
     * @return 参数值
     */
    public static String getStringParamTrim(HttpServletRequest request, String name) {
        return getStringParamTrim(request, name, null);
    }

    /**
     * 获取http请求String参数，并调用{@link String#trim() String.trim()}方法，null或字符串空（全空格也算字符串空）值则返回默认值，不会报错
     *
     * @param request 请求对象
     * @param name    参数名称
     * @param def     转换错误时的默认值
     * @return 参数值
     */
    public static String getStringParamTrim(HttpServletRequest request, String name, String def) {
        String value = request.getParameter(name);
        try {
            if (StrUtil.isBlank(value)) {
                return def;
            }
            return StrUtil.trim(value);
        } catch (Exception e) {
        }
        return def;
    }

    /**
     * 获取http请求Boolean参数，没值或值类型错误则抛出异常{@link IllegalArgumentException}
     *
     * @param request 请求对象
     * @param name    参数名称
     * @return 参数值
     */
    public static Boolean getBooleanParamNotNull(HttpServletRequest request, String name, String msg) {
        Boolean value = getBooleanParam(request, name, null);
        return Assert.notNull(value, msg);
    }

    /**
     * 获取http请求Boolean参数，没值或值类型错误则返回null，不会报错
     *
     * @param request 请求对象
     * @param name    参数名称
     * @return 参数值
     */
    public static Boolean getBooleanParam(HttpServletRequest request, String name) {
        return getBooleanParam(request, name, null);
    }

    /**
     * 获取http请求Boolean参数，没值或值类型错误则返回默认值，不会报错
     *
     * @param request 请求对象
     * @param name    参数名称
     * @param def     转换错误时的默认值
     * @return 参数值
     */
    public static Boolean getBooleanParam(HttpServletRequest request, String name, Boolean def) {
        String value = request.getParameter(name);
        try {
            return Convert.toBool(value, def);
        } catch (Exception e) {
        }
        return def;
    }

    /**
     * 获取http请求Double参数，没值或值类型错误则抛出异常{@link IllegalArgumentException}
     *
     * @param request 请求对象
     * @param name    参数名称
     * @return 参数值
     */
    public static Double getDoubleParamNotNull(HttpServletRequest request, String name, String msg) {
        Double value = getDoubleParam(request, name, null);
        return Assert.notNull(value, msg);
    }

    /**
     * 获取http请求Double参数，没值或值类型错误则返回null，不会报错
     *
     * @param request 请求对象
     * @param name    参数名称
     * @return 参数值
     */
    public static Double getDoubleParam(HttpServletRequest request, String name) {
        return getDoubleParam(request, name, null);
    }

    /**
     * 获取http请求Double参数，没值或值类型错误则返回默认值，不会报错
     *
     * @param request 请求对象
     * @param name    参数名称
     * @param def     转换错误时的默认值
     * @return 参数值
     */
    public static Double getDoubleParam(HttpServletRequest request, String name, Double def) {
        String value = request.getParameter(name);
        try {
            return Convert.toDouble(value, def);
        } catch (Exception e) {
        }
        return def;
    }

    /**
     * 是否是ajax请求，判断是否包含{@link #HTTP_HEADER_X_REQUESTED_WITH}，并且值等于{@link #HTTP_HEADER_X_REQUESTED_WITH_AJAX_VALUE}
     *
     * @param request HttpServletRequest对象
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        boolean isAjaxRequest = false;
        String header = getHeader(request, HTTP_HEADER_X_REQUESTED_WITH);
        if (HTTP_HEADER_X_REQUESTED_WITH_AJAX_VALUE.equals(header)) {
            isAjaxRequest = true;
        }
        return isAjaxRequest;
    }

    /**
     * 从Spring RequestContextHolder中取出request对象
     *
     * @return HttpServletRequest对象
     */
    public static HttpServletRequest getRequestFromSpring() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (requestAttributes == null) {
            return null;
        }

        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    /**
     * 获取用户当前访问的目录，如/login，从spring对象中取出request，然后再取uri
     *
     * @return String
     */
    public static String getCurrentRequestPath() {
        return getRequestPath(getRequestFromSpring());
    }

    /**
     * 获取用户当前访问的目录，如/login，如request为null，则返回null
     *
     * @return String
     */
    public static String getRequestPath(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return formatUrlPath(request.getRequestURI());
    }

    /**
     * 把URL格式化成正常格式
     *
     * @return String
     */
    public static String formatUrlPath(String url) {
        return url.replaceAll("//", "/");
    }

    /**
     * 在域上基础上加上二级域名，比如http://baidu.com，www=http://www.baidu.com
     *
     * @param url          完整域名
     * @param secondDomain 二级域名名称
     * @return
     */
    public static String appendSecondDomain(String url, String secondDomain) {
        String appendUrl = URLUtil.normalize(url);

        if (appendUrl.indexOf(HTTP_SCHEME_FULL) == CommonConstant.INT_0) {
            appendUrl = appendUrl.substring(HTTP_SCHEME_FULL.length());
            appendUrl = secondDomain + CommonConstant.POINT_EN + appendUrl;
            appendUrl = HTTP_SCHEME_FULL + appendUrl;
            return appendUrl;
        } else if (appendUrl.indexOf(HTTPS_SCHEME_FULL) == CommonConstant.INT_0) {
            appendUrl = appendUrl.substring(HTTPS_SCHEME_FULL.length());
            appendUrl = secondDomain + CommonConstant.POINT_EN + appendUrl;
            appendUrl = HTTPS_SCHEME_FULL + appendUrl;
            return appendUrl;
        }
        return url;
    }

    public static String getDomainFromUrl(String urlStr) {
        URL url = URLUtil.url(urlStr);

        String domain = url.getProtocol() + HTTP_SCHEME_SUFFIX + url.getHost();
        if (url.getPort() != -1) {
            domain += (CommonConstant.COLON_EN + url.getPort());
        }
        return domain;
    }

    /**
     * 转换为String
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static String inputStream2String(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        try {
            byte[] b = new byte[4096];
            int n;
            while ((n = in.read(b)) != -1) {
                out.append(new String(b, 0, n));
            }

            return out.toString();
        } catch (IOException ex) {
            throw ex;
        }

    }

    /**
     * 判断user-agent是否是手机
     *
     * @param userAgent 浏览器user-agent
     * @return
     */
    public static boolean isMobile(String userAgent) {
        if (StrUtil.isBlank(userAgent)) {
            return false;
        }

        try {
            UserAgent userAgentObj = UserAgentUtil.parse(userAgent);
            return userAgentObj == null ? false : userAgentObj.isMobile();
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        UserAgent userAgent = UserAgentUtil.parse("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36");
        System.out.println(userAgent.isMobile());
        System.out.println(userAgent.getOs());
    }
}
