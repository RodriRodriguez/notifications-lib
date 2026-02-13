package com.notifications.channels.push;

import com.notifications.core.Notification;
import com.notifications.core.NotificationChannel;
import com.notifications.core.NotificationProvider;
import com.notifications.core.NotificationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public final class PushChannel implements NotificationChannel {
    
    private static final Logger logger = LoggerFactory.getLogger(PushChannel.class);
    
    private final PushConfig config;
    private final NotificationProvider<PushNotification> provider;

    public PushChannel(PushConfig config) {
        this.config = Objects.requireNonNull(config, "PushConfig cannot be null");
        if (!config.isValid()) {
            throw new IllegalArgumentException("Invalid PushConfig");
        }

        this.provider = createProvider(config);
        logger.info("PushChannel initialized with provider: {}", config.getProvider().getName());
    }

    private NotificationProvider<PushNotification> createProvider(PushConfig config) {
        return switch (config.getProvider()) {
            case FIREBASE -> new com.notifications.providers.push.FirebaseProvider(config);
        };
    }

    @Override
    public String getChannelType() {
        return "push";
    }

    @Override
    public NotificationResult send(Notification notification) {
        if (!canHandle(notification)) {
            throw new IllegalArgumentException(
                    String.format("PushChannel cannot handle notification type: %s", 
                            notification.getChannelType())
            );
        }

        PushNotification pushNotification = (PushNotification) notification;

        if (pushNotification.getRecipients().isEmpty()) {
            logger.warn("Push notification has no recipients");
            return NotificationResult.failure(
                    "At least one recipient (device token) is required",
                    "VALIDATION_ERROR",
                    getChannelType(),
                    config.getProvider().getId()
            );
        }

        if (pushNotification.getContent() == null || 
            pushNotification.getTitle() == null || pushNotification.getTitle().trim().isEmpty()) {
            logger.warn("Push notification missing title");
            return NotificationResult.failure(
                    "Push notification title cannot be empty",
                    "VALIDATION_ERROR",
                    getChannelType(),
                    config.getProvider().getId()
            );
        }

        try {
            logger.debug("Sending push notification via provider: {}", provider.getProviderName());
            return provider.send(pushNotification);
        } catch (Exception e) {
            logger.error("Error sending push notification", e);
            return NotificationResult.failure(
                    String.format("Failed to send push notification: %s", e.getMessage()),
                    "PROVIDER_ERROR",
                    getChannelType(),
                    config.getProvider().getId()
            );
        }
    }

    @Override
    public boolean isReady() {
        return provider.isConfigured();
    }
}
