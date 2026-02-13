package com.notifications.providers.push;

import com.notifications.channels.push.PushConfig;
import com.notifications.channels.push.PushNotification;
import com.notifications.core.NotificationProvider;
import com.notifications.core.NotificationResult;
import com.notifications.providers.push.dto.FcmMessagePayload;
import com.notifications.providers.push.dto.FcmSendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
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

        try {
            if (notification.getRecipients().isEmpty()) {
                return buildErrorResult("At least one device token is required", "VALIDATION_ERROR", 400);
            }

            String title = notification.getTitle();
            String body = notification.getBody();
            
            if (title == null || title.trim().isEmpty()) {
                return buildErrorResult("Notification title cannot be empty", "VALIDATION_ERROR", 400);
            }

            FcmMessagePayload payload = buildFcmPayload(notification);

            logger.debug("FCM Payload: Token={}, Title={}, Body length={}", 
                    payload.getMessage().getToken(),
                    payload.getMessage().getNotification().getTitle(),
                    payload.getMessage().getNotification().getBody().length());

            Thread.sleep(50);

            String messageId = UUID.randomUUID().toString().replace("-", "");
            FcmSendResponse response = FcmSendResponse.success(config.getProjectId(), messageId);
            
            logger.info("Firebase push notification sent successfully. Message ID: {}", messageId);

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("fcm_message_id", messageId);
            metadata.put("fcm_name", response.getName());
            metadata.put("status_code", response.getStatusCode());
            
            return new NotificationResult.Builder()
                    .success(true)
                    .messageId(messageId)
                    .channel(getChannelType())
                    .provider(getProviderName())
                    .statusCode(response.getStatusCode())
                    .providerMetadata(metadata)
                    .build();
                    
        } catch (Exception e) {
            logger.error("Error sending push notification via Firebase", e);
            return buildErrorResult(
                    String.format("Firebase FCM API error: %s", e.getMessage()),
                    "PROVIDER_ERROR",
                    500
            );
        }
    }

    private FcmMessagePayload buildFcmPayload(PushNotification notification) {
        String deviceToken = notification.getRecipients().isEmpty() 
                ? "" 
                : notification.getRecipients().get(0).getAddress();

        FcmMessagePayload.FcmNotification fcmNotification = new FcmMessagePayload.FcmNotification(
                notification.getTitle(),
                notification.getBody()
        );

        FcmMessagePayload.FcmMessage message = new FcmMessagePayload.FcmMessage(
                deviceToken,
                fcmNotification,
                null
        );
        
        return new FcmMessagePayload(message);
    }

    private NotificationResult buildErrorResult(String errorMessage, String errorCode, int statusCode) {
        return new NotificationResult.Builder()
                .success(false)
                .error(errorMessage)
                .errorCode(errorCode)
                .channel(getChannelType())
                .provider(getProviderName())
                .statusCode(statusCode)
                .build();
    }

    @Override
    public boolean isConfigured() {
        return config != null && 
               config.getProjectId() != null && !config.getProjectId().trim().isEmpty() &&
               config.getServiceAccountJson() != null && !config.getServiceAccountJson().trim().isEmpty();
    }
}
