package com.notifications.providers.email;

import com.notifications.channels.email.EmailConfig;
import com.notifications.channels.email.EmailNotification;
import com.notifications.core.NotificationProvider;
import com.notifications.core.NotificationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

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
        logger.debug("  Endpoint: POST https://api.sendgrid.com/v3/mail/send");
        logger.debug("  Authorization: Bearer {}", maskApiKey(config.getApiKey()));
        logger.debug("  From: {} <{}>", config.getFromName(), config.getFromEmail());
        logger.debug("  To: {}", notification.getRecipients());
        logger.debug("  Subject: {}", notification.getContent().getSubject());

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String messageId = "sg-" + UUID.randomUUID().toString().substring(0, 8);
        logger.info("SendGrid email sent successfully. Message ID: {}", messageId);
        
        return NotificationResult.success(messageId, getChannelType(), getProviderName());
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
