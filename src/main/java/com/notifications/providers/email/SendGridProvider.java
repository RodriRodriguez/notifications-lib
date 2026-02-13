package com.notifications.providers.email;

import com.notifications.channels.email.EmailConfig;
import com.notifications.channels.email.EmailNotification;
import com.notifications.core.NotificationProvider;
import com.notifications.core.NotificationResult;
import com.notifications.providers.email.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public final class SendGridProvider implements NotificationProvider<EmailNotification> {
    
    private static final Logger logger = LoggerFactory.getLogger(SendGridProvider.class);
    
    private final EmailConfig config;

    public SendGridProvider(EmailConfig config) {
        this.config = config;
    }

    @Override
    public String getProviderName() {
        return "sendgrid";
    }

    @Override
    public String getChannelType() {
        return "email";
    }

    @Override
    public NotificationResult send(EmailNotification notification) {
        logger.info("Simulating SendGrid API call:");
        logger.debug("Authorization: Bearer {}", maskApiKey(config.getApiKey()));

        try {
            if (config.getFromEmail() == null || !isValidEmail(config.getFromEmail())) {
                return buildErrorResult("Invalid 'from' email address", "VALIDATION_ERROR", 400);
            }

            SendGridMailPayload payload = buildSendGridPayload(notification);

            logger.debug("SendGrid Payload: From={}, To={} recipient(s), Subject={}", 
                    payload.getFrom().getEmail(),
                    payload.getPersonalizations().isEmpty() ? 0 : payload.getPersonalizations().get(0).getTo().size(),
                    payload.getPersonalizations().isEmpty() ? "" : payload.getPersonalizations().get(0).getSubject());

            Thread.sleep(50);

            String messageId = "filter0001." + UUID.randomUUID().toString().substring(0, 8) + ".sendgrid.net";
            logger.info("SendGrid email sent successfully. Message ID: {}", messageId);

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("sendgrid_message_id", messageId);
            metadata.put("status_code", 202);
            
            return new NotificationResult.Builder()
                    .success(true)
                    .messageId(messageId)
                    .channel(getChannelType())
                    .provider(getProviderName())
                    .statusCode(202)
                    .providerMetadata(metadata)
                    .build();
                    
        } catch (Exception e) {
            logger.error("Error sending email via SendGrid", e);
            return buildErrorResult(
                    String.format("SendGrid API error: %s", e.getMessage()),
                    "PROVIDER_ERROR",
                    500
            );
        }
    }

    private SendGridMailPayload buildSendGridPayload(EmailNotification notification) {
        // Build 'to' recipients
        List<SendGridEmailAddress> toAddresses = notification.getRecipients().stream()
                .map(recipient -> new SendGridEmailAddress(
                        recipient.getAddress(),
                        recipient.getName()
                ))
                .collect(Collectors.toList());

        List<SendGridEmailAddress> ccAddresses = new ArrayList<>();
        if (!notification.getCcRecipients().isEmpty()) {
            ccAddresses = notification.getCcRecipients().stream()
                    .map(SendGridEmailAddress::new)
                    .collect(Collectors.toList());
        }

        SendGridPersonalization personalization = new SendGridPersonalization(
                toAddresses,
                ccAddresses,
                notification.getContent().getSubject()
        );

        SendGridEmailAddress fromAddress = new SendGridEmailAddress(
                config.getFromEmail(),
                config.getFromName()
        );

        List<SendGridContentBlock> contentBlocks = new ArrayList<>();
        if (notification.getContent().getBody() != null && !notification.getContent().getBody().trim().isEmpty()) {
            if (notification.getContent().isHtml()) {
                contentBlocks.add(SendGridContentBlock.html(notification.getContent().getBody()));
            } else {
                contentBlocks.add(SendGridContentBlock.plainText(notification.getContent().getBody()));
            }
        }

        return new SendGridMailPayload(
                List.of(personalization),
                fromAddress,
                contentBlocks
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

    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    @Override
    public boolean isConfigured() {
        return config != null && config.getApiKey() != null && !config.getApiKey().trim().isEmpty();
    }

    private String maskApiKey(String apiKey) {
        if (apiKey == null || apiKey.length() < 8) {
            return "***";
        }
        return apiKey.substring(0, 4) + "..." + apiKey.substring(apiKey.length() - 4);
    }
}
