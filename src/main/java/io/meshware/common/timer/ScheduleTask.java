package io.meshware.common.timer;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

/**
 * ScheduleTask
 *
 * @author Zhiguo.Chen
 * @since 20211019
 */
@Slf4j
public abstract class ScheduleTask implements TimeTask {

    private long time;
    private final Timer currentTimer;
    private volatile boolean running = true;

    public ScheduleTask(Timer currentTimer) {
        this.currentTimer = currentTimer;
    }

    /**
     * 执行时间
     *
     * @return long
     */
    @Override
    public long getTime() {
        return time;
    }

    @Override
    public void run() {
        if (running) {
            try {
                CompletableFuture<Void> completableFuture = execute();
                if (!isAsync()) {
                    completableFuture.get();
                }
            } catch (Exception e) {
                log.error("Task execute error! message={}", e.getMessage(), e);
            }
            time = TimeUtil.now() + getIntervalInMs();
            currentTimer.add(this);
        }
    }

    public void stop() {
        running = false;
    }

    /**
     * Execute
     *
     * @return future
     */
    public abstract CompletableFuture<Void> execute();

    /**
     * Return interval time in millisecond
     *
     * @return interval time
     */
    public abstract int getIntervalInMs();

    /**
     * Async or sync
     *
     * @return bool
     */
    public abstract boolean isAsync();
}
