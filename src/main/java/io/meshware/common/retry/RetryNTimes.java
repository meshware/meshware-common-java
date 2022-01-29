package io.meshware.common.retry;

import java.util.concurrent.TimeUnit;

/**
 * Retry N Times
 *
 * <pre>If N &gt; 0 will retry N times, and if N &lt; 0, it will retry indefinitely.</pre>
 */
public class RetryNTimes extends AbstractRetryPolicy {

    private final long sleepMilliseconds;

    public RetryNTimes(int maxRetried, int sleepTime, TimeUnit unit) {
        super(maxRetried);
        this.sleepMilliseconds = unit.convert(sleepTime, TimeUnit.MILLISECONDS);
    }

    @Override
    protected long getSleepTime(int retried, long elapsed) {
        return sleepMilliseconds;
    }
}
