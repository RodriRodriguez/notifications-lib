package com.notifications.providers.sms;

import com.notifications.channels.sms.SmsConfig;
import com.notifications.channels.sms.SmsNotification;
import com.notifications.core.NotificationProvider;
import com.notifications.core.NotificationResult;
import com.notifications.providers.sms.dto.TwilioMessagePayload;
import com.notifications.providers.sms.dto.TwilioMessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class TwilioProvider implements NotificationProvider<SmsNotification> {
    
    private static final Logger logger = LoggerFactory.getLogger(TwilioProvider.class);
    private static final int MAX_MESSAGE_LENGTH = 1600;
    
    private final SmsConfig config;

    public TwilioProvider(SmsConfig config) {
        this.config = config;
    }

    @Override
    public String getProviderName() {
        return "twilio";
    }

    @Override
    public String getChannelType() {
        return "sms";
    }

    @Override
    public NotificationResult send(SmsNotification notification) {
        logger.info("Simulating Twilio API call:");
        logger.debug("  Auth: Basic (AccountSid:AuthToken)");

        try {
            String message = notification.getMessage();
            if (message == null || message.trim().isEmpty()) {
                return buildErrorResult("Message body cannot be empty", "VALIDATION_ERROR", 400);
            }

            if (message.length() > MAX_MESSAGE_LENGTH) {
                return buildErrorResult(
                        String.format("Message length (%d) exceeds Twilio limit of %d characters", 
                                message.length(), MAX_MESSAGE_LENGTH),
                        "VALIDATION_ERROR",
                        400
                );
            }

            TwilioMessagePayload payload = buildTwilioPayload(notification);

            logger.debug("Twilio Payload: From={}, To={}, Body length={}", 
                    payload.getFrom(),
                    payload.getTo(),
                    payload.getBody().length());

            Thread.sleep(50);

            String messageSid = "SM" + UUID.randomUUID().toString().replace("-", "").substring(0, 32);
            TwilioMessageResponse response = TwilioMessageResponse.success(messageSid);
            
            logger.info("Twilio SMS sent successfully. Message SID: {}", messageSid);

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("twilio_message_sid", messageSid);
            metadata.put("twilio_status", response.getStatus());
            metadata.put("status_code", response.getStatusCode());
            
            return new NotificationResult.Builder()
                    .success(true)
                    .messageId(messageSid)
                    .channel(getChannelType())
                    .provider(getProviderName())
                    .statusCode(response.getStatusCode())
                    .providerMetadata(metadata)
                    .build();
                    
        } catch (Exception e) {
            logger.error("Error sending SMS via Twilio", e);
            return buildErrorResult(
                    String.format("Twilio API error: %s", e.getMessage()),
                    "PROVIDER_ERROR",
                    500
            );
        }
    }

    private TwilioMessagePayload buildTwilioPayload(SmsNotification notification) {
        String to = notification.getRecipients().isEmpty() 
                ? "" 
                : notification.getRecipients().get(0).getAddress();
        
        String body = notification.getMessage();
        
        return new TwilioMessagePayload(
                config.getFromPhoneNumber(),
                to,
                body
        );
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
               config.getAccountSid() != null && !config.getAccountSid().trim().isEmpty() &&
               config.getAuthToken() != null && !config.getAuthToken().trim().isEmpty();
    }
}
