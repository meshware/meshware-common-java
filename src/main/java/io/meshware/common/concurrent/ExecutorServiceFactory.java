package io.meshware.common.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceFactory {

    public static ExecutorService createExecutorService(int poolSize, int queueSize, String name) {
        return new ThreadPoolExecutor(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(queueSize), new NamedThreadFactory(name),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public static ExecutorService createExecutorService(int poolSize, int queueSize, String name, boolean daemon) {
        return new ThreadPoolExecutor(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(queueSize), new NamedThreadFactory(name, daemon),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

}
