package com.im.common.util.instant;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.time.Duration;
import java.time.Instant;

/**
 * 性能统计工具类
 *
 * @author Barry
 * @date 2020-06-13
 */
public class InstantUtil {
    private static Log LOG = LogFactory.get();

    private static ThreadLocal<Instant> CURRENT_THREAD_START = new ThreadLocal<>();

    public static void start() {
        CURRENT_THREAD_START.set(Instant.now());
    }

    public static void end(String desc) {
        Instant start = CURRENT_THREAD_START.get();
        CURRENT_THREAD_START.remove();

        Instant end = Instant.now();

        long mills = Duration.between(start, end).toMillis();

        LOG.info(desc + "，耗时" + mills);
    }
}
