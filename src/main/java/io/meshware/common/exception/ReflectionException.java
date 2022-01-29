package io.meshware.common.exception;

public class ReflectionException extends SystemException {

    public ReflectionException() {
    }

    public ReflectionException(String message) {
        super(message);
    }

    public ReflectionException(String message, String errorCode) {
        super(message, errorCode);
    }

    public ReflectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectionException(String message, Throwable cause, String errorCode) {
        super(message, cause, errorCode);
    }

    public ReflectionException(Throwable cause) {
        super(cause);
    }

    public ReflectionException(Throwable cause, String errorCode) {
        super(cause, errorCode);
    }

}
