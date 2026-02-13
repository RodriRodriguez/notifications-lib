package com.notifications.exception;

public class NotificationException extends RuntimeException {
    
    private final String errorCode;

    public NotificationException(String message) {
        super(message);
        this.errorCode = null;
    }

    public NotificationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public NotificationException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = null;
    }

    public NotificationException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
