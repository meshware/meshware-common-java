package io.meshware.common.retry;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

@Slf4j
public class RetryLoops {

    private final long startTimeMs = System.currentTimeMillis();
    private boolean isDone = false;
    private int retriedCount = 0;

    public static <R> R invokeWithRetry(Callable<R> task, RetryPolicy retryPolicy) throws Exception {
        R result = null;
        RetryLoops retryLoop = new RetryLoops();
        while (retryLoop.shouldContinue()) {
            try {
                result = task.call();
                retryLoop.complete();
            } catch (Exception e) {
                retryLoop.fireException(e, retryPolicy);
            }
        }
        return result;
    }

    public void fireException(Exception e, RetryPolicy retryPolicy) throws Exception {

        if (e instanceof InterruptedException) {
            Thread.currentThread().interrupt();
        }

        boolean rethrow = true;
        if (isRetryException(e)
                && retryPolicy.shouldRetry(retriedCount++, System.currentTimeMillis() - startTimeMs, true)) {
            rethrow = false;
        }

        if (rethrow) {
            throw e;
        }
    }

    private boolean isRetryException(Throwable e) {
        //Status status = Status.fromThrowable(e);
        //if (OptionUtil.isRecoverable(status)) {
        //    return true;
        //}

        return true;
    }

    public boolean shouldContinue() {
        return !isDone;
    }

    public void complete() {
        isDone = true;
    }

}
