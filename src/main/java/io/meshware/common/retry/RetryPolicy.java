package io.meshware.common.retry;

public interface RetryPolicy {

    /**
     * Whether retry is supported when operation fails.
     *
     * @param retried the number of times retried so far
     * @param elapsed the elapsed time in millisecond since the operation was attempted
     * @param sleep   should be sleep
     * @return true should be retry
     */
    boolean shouldRetry(int retried, long elapsed, boolean sleep);

}
