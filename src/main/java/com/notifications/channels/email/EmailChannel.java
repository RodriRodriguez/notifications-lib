package com.notifications.channels.email;

import com.notifications.core.Notification;
import com.notifications.core.NotificationChannel;
import com.notifications.core.NotificationProvider;
import com.notifications.core.NotificationResult;
import com.notifications.validation.EmailValidator;
import com.notifications.validation.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public final class EmailChannel implements NotificationChannel {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailChannel.class);
    
    private final EmailConfig config;
    private final NotificationProvider<EmailNotification> provider;

    public EmailChannel(EmailConfig config) {
        this.config = Objects.requireNonNull(config, "EmailConfig cannot be null");
        if (!config.isValid()) {
            throw new IllegalArgumentException("Invalid EmailConfig");
        }

        this.provider = createProvider(config);
        logger.info("EmailChannel initialized with provider: {}", config.getProvider().getName());
    }

    private NotificationProvider<EmailNotification> createProvider(EmailConfig config) {
        return switch (config.getProvider()) {
            case SENDGRID -> new com.notifications.providers.email.SendGridProvider(config);
        };
    }

    @Override
    public String getChannelType() {
        return "email";
    }

    @Override
    public NotificationResult send(Notification notification) {
        if (!canHandle(notification)) {
            throw new IllegalArgumentException(
                    String.format("EmailChannel cannot handle notification type: %s", 
                            notification.getChannelType())
            );
        }

        EmailNotification emailNotification = (EmailNotification) notification;

        ValidationResult validationResult = EmailValidator.validate(emailNotification);
        if (validationResult.isInvalid()) {
            logger.warn("Email validation failed: {}", validationResult.getErrorMessage().orElse("Unknown error"));
            return NotificationResult.failure(
                    validationResult.getErrorMessage().orElse("Email validation failed"),
                    "VALIDATION_ERROR",
                    getChannelType(),
                    config.getProvider().getId()
            );
        }

        try {
            logger.debug("Sending email notification via provider: {}", provider.getProviderName());
            return provider.send(emailNotification);
        } catch (Exception e) {
            logger.error("Error sending email notification", e);
            return NotificationResult.failure(
                    String.format("Failed to send email: %s", e.getMessage()),
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
