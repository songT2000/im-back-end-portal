package com.im.common.util;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * websocket客户端，有简单的重连机制
 *
 * @author Barry
 * @date 2021-08-21
 */
public class WebSocketClient {
    private static final Log LOG = LogFactory.get();

    private String url;
    /**
     * 定时器
     */
    private ScheduledExecutorService heartBeatTimer;
    private WebSocket webSocket;
    private AtomicBoolean connecting = new AtomicBoolean(false);

    /**
     * @param url 连接地址
     */
    public WebSocketClient(String url) {
        this.url = url;
    }

    public WebSocket connect(int timeOutSecs, WebSocketListener listener) throws IOException {
        connecting.set(true);

        try {
            // 连接
            webSocket = WebSocketUtil.connect(url, timeOutSecs, listener);

            // 心跳
            heartBeat(timeOutSecs, listener);

            return webSocket;
        } catch (IOException e) {
            throw e;
        } finally {
            connecting.set(false);
        }
    }

    private void heartBeat(int timeOutSecs, WebSocketListener listener) throws IOException {
        if (heartBeatTimer == null) {
            heartBeatTimer = new ScheduledThreadPoolExecutor(2,
                    r -> ThreadUtil.newThread(r, StrUtil.format("WebSocketClient-HeartBeat-Timer-{}", ClockUtil.nowMillis())));
        }

        // 调度周期
        long periodMs = Duration.ofSeconds(5).toMillis();

        // 创建调度
        heartBeatTimer.scheduleAtFixedRate(() -> {
            reconnect(timeOutSecs, listener);
        }, periodMs, periodMs, TimeUnit.MILLISECONDS);
    }

    private synchronized void reconnect(int timeOutSecs, WebSocketListener listener) {
        if (Boolean.TRUE.equals(connecting.get())) {
            return;
        }

        try {
            boolean send = webSocket.send(ClockUtil.nowMillis() + "");
            if (!send) {
                LOG.error("检测到WebSocketClient={}未连接，正在重连", url);
                connect(timeOutSecs, listener);
            }
        } catch (Exception e) {
        }
    }
}