package io.meshware.common.exception;

/**
 * System Exception
 */
public class SystemException extends RuntimeException {

    protected String errorCode;

    protected boolean retry;

    public SystemException() {
    }

    public SystemException(boolean retry) {
        this.retry = retry;
    }

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, boolean retry) {
        super(message);
        this.retry = retry;
    }

    public SystemException(String message, String errorCode, boolean retry) {
        super(message);
        this.errorCode = errorCode;
        this.retry = retry;
    }

    public SystemException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemException(String message, Throwable cause, boolean retry) {
        super(message, cause);
        this.retry = retry;
    }

    public SystemException(String message, Throwable cause, String errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public SystemException(String message, Throwable cause, String errorCode, boolean retry) {
        super(message, cause);
        this.errorCode = errorCode;
        this.retry = retry;
    }

    public SystemException(Throwable cause) {
        super(cause);
    }

    public SystemException(Throwable cause, boolean retry) {
        super(cause);
        this.retry = retry;
    }

    public SystemException(Throwable cause, String errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public SystemException(Throwable cause, String errorCode, boolean retry) {
        super(cause);
        this.errorCode = errorCode;
        this.retry = retry;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * 是否可重试
     *
     * @return bool
     */
    public boolean isRetry() {
        return retry;
    }

    public void setRetry(boolean retry) {
        this.retry = retry;
    }

}
