package com.notifications.exception;

public class ProviderException extends NotificationException {
    
    private final String providerName;

    public ProviderException(String message, String providerName) {
        super(message, "PROVIDER_ERROR");
        this.providerName = providerName;
    }

    public ProviderException(String message, String providerName, Throwable cause) {
        super(message, "PROVIDER_ERROR", cause);
        this.providerName = providerName;
    }

    public String getProviderName() {
        return providerName;
    }
}
