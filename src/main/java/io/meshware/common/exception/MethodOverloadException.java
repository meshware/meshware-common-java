package io.meshware.common.exception;

/**
 * 方法过载异常
 */
public class MethodOverloadException extends ReflectiveOperationException {

    public MethodOverloadException() {
    }

    public MethodOverloadException(String message) {
        super(message);
    }

    public MethodOverloadException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodOverloadException(Throwable cause) {
        super(cause);
    }

}
