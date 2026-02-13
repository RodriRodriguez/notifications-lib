package com.notifications.core;

public interface NotificationProvider<T extends Notification> {

    String getProviderName();

    NotificationResult send(T notification);

    default boolean isConfigured() {
        return true;
    }

    String getChannelType();
}
