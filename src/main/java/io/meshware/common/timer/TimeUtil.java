package io.meshware.common.timer;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public final class TimeUtil {

    protected static final TimeUtil instance = new TimeUtil();

    // 精度(毫秒)
    protected long precision;

    // 当前时间
    protected volatile long now;

    // 调度任务
    protected ScheduledExecutorService scheduler;

    public static TimeUtil getInstance() {
        return instance;
    }

    public TimeUtil() {
        this(1L);
    }

    public TimeUtil(long precision) {
        this.precision = precision;
        now = System.currentTimeMillis();
        scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, "SystemClock");
            thread.setDaemon(true);
            return thread;
        });
        scheduler.scheduleAtFixedRate(() -> {
            now = System.currentTimeMillis();
        }, precision, precision, TimeUnit.MILLISECONDS);
    }

    public long getTime() {
        return now;
    }

    public long precision() {
        return precision;
    }

    /**
     * 获取当前时钟
     *
     * @return 当前时钟
     */
    public static long now() {
        return instance.getTime();
    }

    public static long microTime() {
        long microTime = now() * 1000;
        long nanoTime = System.nanoTime();
        return microTime + (nanoTime % 1000000) / 1000;
    }
}
