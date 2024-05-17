package com.im.common.util;

import com.alibaba.fastjson.JSON;
import com.im.common.util.url.UrlParamUtil;
import lombok.NonNull;
import okhttp3.*;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * http请求工具类，不允许再独立创建其它http请求工具类，只能在本类加方法，本类是封装okhttp
 *
 * @author Barry
 * @date 2017/6/28
 */
public class HttpClientUtil {
    /**
     * JSON
     **/
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    /**
     * FORM
     **/
    private static final MediaType MEDIA_TYPE_FORM = MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8");

    /**
     * 向远程服务器发送一次get
     *
     * @param url         请求域名
     * @param timeoutSecs 超时秒数
     * @return String
     * @throws IOException
     */
    public static String get(String url, int timeoutSecs) throws IOException {
        return sendRequest(buildGetRequest(url, null), null, timeoutSecs);
    }

    /**
     * 向远程服务器发送一次get
     *
     * @param url         请求域名
     * @param params      本次要发送的参数
     * @param timeoutSecs 超时秒数
     * @return String
     * @throws IOException
     */
    public static String get(String url, Map<String, String> params, int timeoutSecs) throws IOException {
        return sendRequest(buildGetRequest(url, params), null, timeoutSecs);
    }

    /**
     * 向远程服务器发送一次get
     *
     * @param url         请求域名
     * @param params      本次要发送的参数
     * @param timeoutSecs 超时秒数
     * @return String
     * @throws IOException
     */
    public static String get(String url, Map<String, String> params, Map<String, String> header, int timeoutSecs) throws IOException {
        return sendRequest(buildGetRequest(url, params), header, timeoutSecs);
    }

    /**
     * 向远程服务器发送一次get，并获取返回的302的地址
     *
     * @param url         请求域名
     * @param header      header
     * @param cookieJar   cookie管理器
     * @param timeoutSecs 超时秒数
     * @return String
     * @throws IOException
     */
    public static String get(String url, Map<String, String> header, CookieJar cookieJar, int timeoutSecs) throws IOException {
        Request.Builder builder = new Request.Builder();
        setHeaders(builder, header);
        Request request = builder.url(url).get().build();

        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .cookieJar(cookieJar)
                .connectTimeout(timeoutSecs, TimeUnit.SECONDS)
                .writeTimeout(timeoutSecs, TimeUnit.SECONDS)
                .readTimeout(timeoutSecs, TimeUnit.SECONDS)
                .build();
        client.writeTimeoutMillis();
        Call call = client.newCall(request);

        try {
            Response rsp = call.execute();
            return rsp.body().string();
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 向远程服务器发送一次get，并获取返回的302的地址
     *
     * @param url         请求域名
     * @param header      header
     * @param cookieJar   cookie管理器
     * @param timeoutSecs 超时秒数
     * @return String
     * @throws IOException
     */
    public static String getFollowRedirectReturnCurrentUrl(String url, Map<String, String> header, CookieJar cookieJar, int timeoutSecs) throws IOException {
        Request.Builder builder = new Request.Builder();
        setHeaders(builder, header);
        Request request = builder.url(url).get().build();

        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .cookieJar(cookieJar)
                .followRedirects(true)
                .connectTimeout(timeoutSecs, TimeUnit.SECONDS)
                .writeTimeout(timeoutSecs, TimeUnit.SECONDS)
                .readTimeout(timeoutSecs, TimeUnit.SECONDS)
                .build();
        client.writeTimeoutMillis();
        Call call = client.newCall(request);

        try {
            Response rsp = call.execute();

            return rsp.request().url().toString();
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 向远程服务器发送一次post，并获取返回的302的地址
     *
     * @param url         请求域名
     * @param header      header
     * @param timeoutSecs 超时秒数
     * @return String
     * @throws IOException
     */
    public static String postWithFormFoundUrl(String url, Map<String, String> content, Map<String, String> header, CookieJar cookieJar, int timeoutSecs) throws IOException {
        Request.Builder builder = new Request.Builder();
        setHeaders(builder, header);
        Request request = buildPostFormRequest(url, MEDIA_TYPE_FORM, content);

        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .cookieJar(cookieJar)
                .followRedirects(false)
                .connectTimeout(timeoutSecs, TimeUnit.SECONDS)
                .writeTimeout(timeoutSecs, TimeUnit.SECONDS)
                .readTimeout(timeoutSecs, TimeUnit.SECONDS)
                .build();
        client.writeTimeoutMillis();
        Call call = client.newCall(request);

        try {
            Response rsp = call.execute();

            if (rsp.code() == HttpStatus.FOUND.value()) {
                return rsp.header("Location");
            }
            return null;
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 向远程服务器发送一次post，并获取返回的302的地址
     *
     * @param url         请求域名
     * @param header      header
     * @param timeoutSecs 超时秒数
     * @return String
     * @throws IOException
     */
    public static String postWithForm(String url, Map<String, String> content, Map<String, String> header, CookieJar cookieJar, int timeoutSecs) throws IOException {
        Request.Builder builder = new Request.Builder();
        setHeaders(builder, header);
        Request request = buildPostFormRequest(url, MEDIA_TYPE_FORM, content);

        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .cookieJar(cookieJar)
                .connectTimeout(timeoutSecs, TimeUnit.SECONDS)
                .writeTimeout(timeoutSecs, TimeUnit.SECONDS)
                .readTimeout(timeoutSecs, TimeUnit.SECONDS)
                .build();
        client.writeTimeoutMillis();
        Call call = client.newCall(request);

        try {
            Response rsp = call.execute();
            return rsp.body().string();
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 向远程服务器发送一次post，并追踪302，返回最终结果
     *
     * @param url         请求域名
     * @param content     cookie管理
     * @param header      header
     * @param cookieJar
     * @param timeoutSecs 超时秒数
     * @return String
     * @throws IOException
     */
    public static String postWithFormFollowRedirect(String url, Map<String, String> content, Map<String, String> header, CookieJar cookieJar, int timeoutSecs) throws IOException {
        Request.Builder builder = new Request.Builder();
        setHeaders(builder, header);
        Request request = buildPostFormRequest(url, MEDIA_TYPE_FORM, content);

        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .cookieJar(cookieJar)
                .followRedirects(true)
                .connectTimeout(timeoutSecs, TimeUnit.SECONDS)
                .writeTimeout(timeoutSecs, TimeUnit.SECONDS)
                .readTimeout(timeoutSecs, TimeUnit.SECONDS)
                .build();
        client.writeTimeoutMillis();
        Call call = client.newCall(request);

        try {
            Response response = call.execute();

            return response.body().string();
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 向远程服务器发送一次Content-Type为application/json的post，向服务器方问清楚对方接收的类型
     *
     * @param url         请求域名
     * @param content     本次要发送的参数
     * @param timeoutSecs 超时秒数
     * @return String
     * @throws IOException
     */
    public static String postWithJson(String url, Object content, int timeoutSecs) throws IOException {
        return postWithJson(url, content, null, timeoutSecs);
    }

    /**
     * 向远程服务器发送一次Content-Type为application/json的post，向服务器方问清楚对方接收的类型
     *
     * @param url         请求域名
     * @param content     本次要发送的参数
     * @param headerMap   http头列表
     * @param timeoutSecs 超时秒数
     * @return String
     * @throws IOException
     */
    public static String postWithJson(String url, Object content, Map<String, String> headerMap, int timeoutSecs) throws IOException {
        return sendRequest(buildPostJsonRequest(url, MEDIA_TYPE_JSON, content), headerMap, timeoutSecs);
    }

    /**
     * <p>向远程服务器发送一次Content-Type为application/x-www-form-urlencoded的post</p>
     *
     * <p>向服务器方问清楚对方接收的类型，如对方也说不清，一般就是form请求</p>
     *
     * @param url         请求域名
     * @param content     本次要发送的参数
     * @param timeoutSecs 超时秒数
     * @return String
     * @throws IOException
     */
    public static String postWithForm(String url, Map<String, String> content, int timeoutSecs) throws IOException {
        return postWithForm(url, content, null, timeoutSecs);
    }

    /**
     * 向远程服务器发送一次Content-Type为application/x-www-form-urlencoded的post
     * <p>
     * 向服务器方问清楚对方接收的类型，如对方也说不清，一般就是form请求
     *
     * @param url         请求域名
     * @param content     本次要发送的参数
     * @param headerMap   http头列表
     * @param timeoutSecs 超时秒数
     * @return String
     * @throws IOException
     */
    public static String postWithForm(String url, Map<String, String> content, Map<String, String> headerMap, int timeoutSecs) throws IOException {
        return sendRequest(buildPostFormRequest(url, MEDIA_TYPE_FORM, content), headerMap, timeoutSecs);
    }

    /**
     * 向远程服务器发送一次Content-Type为指定类型的post
     *
     * @param url         请求域名
     * @param content     本次要发送的参数
     * @param headerMap   http头列表
     * @param contentType Content-Type值
     * @param timeoutSecs 超时秒数
     * @return String
     * @throws IOException
     */
    public static String postWithAny(String url, String content, Map<String, String> headerMap, String contentType, int timeoutSecs) throws IOException {
        RequestBody body = RequestBody.create(content, MediaType.parse(contentType));

        Request build = new Request.Builder().url(url).post(body).build();

        return sendRequest(build, headerMap, timeoutSecs);
    }

    /**
     * 统一发送请求
     *
     * @param request     构建好的Request对象
     * @param headerMap   设置在http请求头里的信息，get请求传null就好
     * @param timeoutSecs 超时秒数
     * @return 返回String
     * @throws IOException
     */
    private static String sendRequest(Request request, Map<String, String> headerMap, int timeoutSecs) throws IOException {
        OkHttpClient okHttpClient = buildHttpClient(timeoutSecs);

        Request.Builder builder = new Request.Builder();

        // 设置http请求头
        setHeaders(builder, headerMap);

        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();

            return response.body().string();
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 统一发送请求，并返回302地址
     *
     * @param request     构建好的Request对象
     * @param headerMap   设置在http请求头里的信息，get请求传null就好
     * @param timeoutSecs 超时秒数
     * @return 返回String
     * @throws IOException
     */
    private static String getFoundUrl(Request request, Map<String, String> headerMap, int timeoutSecs) throws IOException {
        OkHttpClient okHttpClient = buildHttpClient(timeoutSecs, false);

        Request.Builder builder = new Request.Builder();

        // 设置http请求头
        setHeaders(builder, headerMap);

        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();

            if (response.code() == HttpStatus.FOUND.value()) {
                return response.header("Location");
            }

            return null;
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 创建一个post json请求
     *
     * @param url               请求域名
     * @param serverContentType 服务器接受的Content-Type
     * @param content           本次要post的参数
     * @return Request
     */
    private static Request buildPostJsonRequest(String url, MediaType serverContentType, Object content) {
        RequestBody body = RequestBody
                .create(JSON.toJSONString(content), serverContentType);

        return new Request.Builder().url(url).post(body).build();
    }

    /**
     * 创建一个post请求
     *
     * @param url               请求域名
     * @param serverContentType 服务器接受的Content-Type
     * @param content           本次要post的参数
     * @return Request
     */
    private static Request buildPostFormRequest(String url, MediaType serverContentType, Map<String, String> content) {
        RequestBody body = RequestBody
                .create(UrlParamUtil.join(content, false), serverContentType);

        return new Request.Builder().url(url).post(body).build();
    }

    /**
     * 创建一个get请求
     *
     * @param url    请求域名
     * @param params 请求参数，会自动在url上拼接
     * @return Request
     */
    private static Request buildGetRequest(String url, Map<String, String> params) {
        String requestUrl = UrlParamUtil.concatGetUrl(url, params);

        Request request = new Request.Builder()
                .url(requestUrl)
                .get()
                .build();

        return request;
    }

    /**
     * 设置http请求头
     *
     * @param builder   Request.Builder
     * @param headerMap http请求头
     */
    private static void setHeaders(Request.Builder builder, Map<String, String> headerMap) {
        // http请求头
        if (headerMap != null && !headerMap.isEmpty()) {
            headerMap.forEach((key, value) -> builder.addHeader(key, value));
        }
    }

    /**
     * 创建一个ok http client
     *
     * @param timeoutSecs 超时秒数
     * @return OkHttpClient
     */
    public static OkHttpClient buildHttpClient(int timeoutSecs) {
        final Duration timeout = Duration.ofSeconds(timeoutSecs);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(timeout)
                .writeTimeout(timeout)
                .readTimeout(timeout)
                .build();

        return okHttpClient;
    }

    /**
     * 创建一个ok http client
     *
     * @param timeoutSecs     超时秒数
     * @param followRedirects 是否跟随302
     * @return OkHttpClient
     */
    public static OkHttpClient buildHttpClient(int timeoutSecs, boolean followRedirects) {
        final Duration timeout = Duration.ofSeconds(timeoutSecs);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .followRedirects(followRedirects)
                .connectTimeout(timeout)
                .writeTimeout(timeout)
                .readTimeout(timeout)
                .build();

        okHttpClient.writeTimeoutMillis();

        return okHttpClient;
    }
}