package com.notifications.core;

public interface NotificationChannel {

    String getChannelType();

    NotificationResult send(Notification notification);

    default boolean canHandle(Notification notification) {
        return notification != null && 
               getChannelType().equals(notification.getChannelType());
    }

    default boolean isReady() {
        return true;
    }
}
