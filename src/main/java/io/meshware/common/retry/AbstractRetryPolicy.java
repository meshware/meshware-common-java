package io.meshware.common.retry;

/**
 * Abstract Retry Policy
 */
public abstract class AbstractRetryPolicy implements RetryPolicy {

    private final int maxRetried;

    protected AbstractRetryPolicy(int maxRetried) {
        this.maxRetried = maxRetried;
    }

    @Override
    public boolean shouldRetry(int retried, long elapsed, boolean sleep) {
        if (retried < maxRetried || maxRetried < 0) {
            try {
                if (sleep) {
                    Thread.sleep(getSleepTime(retried, elapsed));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
            return true;
        }
        return false;
    }

    protected abstract long getSleepTime(int retried, long elapsed);

}
