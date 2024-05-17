package com.im.common.util;

import cn.hutool.core.thread.ThreadFactoryBuilder;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p><strong>高并发场景</strong>下System.currentTimeMillis()的性能问题的优化</p>
 *
 * <p>
 * System.currentTimeMillis()的调用比new一个普通对象要耗时的多（具体耗时高出多少我还没测试过，有人说是100倍左右）<br>
 * System.currentTimeMillis()之所以慢是因为去跟系统打了一次交道<br>
 * 后台定时更新时钟，JVM退出时，线程自动回收<br>
 * 10亿：43410,206,210.72815533980582%<br>
 * 1亿：4699,29,162.0344827586207%<br>
 * 1000万：480,12,40.0%<br>
 * 100万：50,10,5.0%<br>
 * </p>
 *
 * @author hubin
 * @date 2016-08-01
 */
public final class ClockUtil {
    public static final ClockUtil INSTANCE = new ClockUtil(1);

    private final long period;
    private final AtomicLong now;

    private ClockUtil(long period) {
        this.period = period;
        this.now = new AtomicLong(System.currentTimeMillis());
        scheduleClockUpdating();
    }

    /**
     * 获取系统当前毫秒数
     *
     * @return 毫秒数
     */
    public static long nowMillis() {
        return INSTANCE.currentTimeMillis();
    }

    private long currentTimeMillis() {
        return now.get();
    }

    private void scheduleClockUpdating() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setDaemon(true).setNamePrefix("System Clock").build();

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, threadFactory);

        executor.scheduleAtFixedRate(() -> now.set(System.currentTimeMillis()), period, period, TimeUnit.MILLISECONDS);
    }


    public static void main(String[] args) {
        try {
            // 系统初始时会有一些差异
            System.out.println(System.currentTimeMillis());
            System.out.println(ClockUtil.nowMillis());

            Thread.sleep(1000);

            // 之后就正常了
            System.out.println(System.currentTimeMillis());
            System.out.println(ClockUtil.nowMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

