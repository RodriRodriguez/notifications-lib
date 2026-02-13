package com.notifications.providers.push;

import com.notifications.channels.push.PushConfig;
import com.notifications.channels.push.PushNotification;
import com.notifications.core.NotificationProvider;
import com.notifications.core.NotificationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public final class FirebaseProvider implements NotificationProvider<PushNotification> {
    
    private static final Logger logger = LoggerFactory.getLogger(FirebaseProvider.class);
    
    private final PushConfig config;

    public FirebaseProvider(PushConfig config) {
        this.config = config;
    }

    @Override
    public String getProviderName() {
        return "firebase";
    }

    @Override
    public String getChannelType() {
        return "push";
    }

    @Override
    public NotificationResult send(PushNotification notification) {
        logger.info("Simulating Firebase FCM API call:");
        logger.debug("  Endpoint: POST https://fcm.googleapis.com/v1/projects/{}/messages:send", 
                config.getProjectId());
        logger.debug("  Auth: Bearer (OAuth 2.0 token)");
        logger.debug("  Token: {}", notification.getRecipients());
        logger.debug("  Title: {}", notification.getTitle());
        logger.debug("  Body: {}", notification.getBody());

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String messageId = "fcm-" + UUID.randomUUID().toString().substring(0, 8);
        logger.info("Firebase push notification sent successfully. Message ID: {}", messageId);
        
        return NotificationResult.success(messageId, getChannelType(), getProviderName());
    }

    @Override
    public boolean isConfigured() {
        return config != null && 
               config.getProjectId() != null && !config.getProjectId().trim().isEmpty() &&
               config.getServiceAccountJson() != null && !config.getServiceAccountJson().trim().isEmpty();
    }
}
