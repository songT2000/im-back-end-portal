package com.im.common.util;

import lombok.NonNull;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import java.io.IOException;
import java.time.Duration;

/**
 * websocket工具类
 *
 * @author Barry
 * @date 2017/6/28
 */
public class WebSocketUtil {

    /**
     * 连接
     *
     * @param url         请求域名
     * @param timeoutSecs 超时秒数
     * @param listener    监听
     * @return String
     * @throws IOException
     */
    public static WebSocket connect(String url, int timeoutSecs, WebSocketListener listener) throws IOException {
        return connect(url, timeoutSecs, 0, listener);
    }

    /**
     * 连接
     *
     * @param url         请求域名
     * @param timeoutSecs 超时秒数
     * @param pingSecs    ping秒数，小于等于0则不主动ping
     * @param listener    监听
     * @return String
     * @throws IOException
     */
    public static WebSocket connect(String url, int timeoutSecs, int pingSecs, WebSocketListener listener) throws IOException {
        Request request = buildRequest(url);

        OkHttpClient okHttpClient = buildHttpClient(timeoutSecs, pingSecs);

        WebSocket webSocket = okHttpClient.newWebSocket(request, listener);

        return webSocket;
    }

    /**
     * 创建一个请求
     *
     * @param url 请求地址
     * @return Request
     */
    private static Request buildRequest(String url) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        return request;
    }

    /**
     * 创建一个ok http client
     *
     * @param timeoutSecs 超时秒数
     * @param pingSecs    ping秒数，小于等于0则不主动ping
     * @return OkHttpClient
     */
    public static OkHttpClient buildHttpClient(int timeoutSecs, int pingSecs) {
        final Duration timeout = Duration.ofSeconds(timeoutSecs);

        if (pingSecs > 0) {
            final Duration ping = Duration.ofSeconds(pingSecs);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .pingInterval(ping)
                    .connectTimeout(timeout)
                    .writeTimeout(timeout)
                    .readTimeout(timeout)
                    .retryOnConnectionFailure(true)
                    .build();

            return okHttpClient;
        } else {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(timeout)
                    .writeTimeout(timeout)
                    .readTimeout(timeout)
                    .retryOnConnectionFailure(true)
                    .build();

            return okHttpClient;
        }
    }
}