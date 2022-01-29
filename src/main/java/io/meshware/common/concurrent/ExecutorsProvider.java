package io.meshware.common.concurrent;

import java.util.concurrent.ExecutorService;

/**
 * Executors provider interface
 *
 * @author Zhiguo.Chen
 */
public interface ExecutorsProvider {

    /**
     * Get executor service pool
     *
     * @return {@link ExecutorService}
     */
    ExecutorService getExecutorService();
}
