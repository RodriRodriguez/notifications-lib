package com.notifications.channels.sms;

import com.notifications.core.Notification;
import com.notifications.core.NotificationChannel;
import com.notifications.core.NotificationProvider;
import com.notifications.core.NotificationResult;
import com.notifications.validation.PhoneValidator;
import com.notifications.validation.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public final class SmsChannel implements NotificationChannel {
    
    private static final Logger logger = LoggerFactory.getLogger(SmsChannel.class);
    
    private final SmsConfig config;
    private final NotificationProvider<SmsNotification> provider;

    public SmsChannel(SmsConfig config) {
        this.config = Objects.requireNonNull(config, "SmsConfig cannot be null");
        if (!config.isValid()) {
            throw new IllegalArgumentException("Invalid SmsConfig");
        }

        this.provider = createProvider(config);
        logger.info("SmsChannel initialized with provider: {}", config.getProvider().getName());
    }

    private NotificationProvider<SmsNotification> createProvider(SmsConfig config) {
        return switch (config.getProvider()) {
            case TWILIO -> new com.notifications.providers.sms.TwilioProvider(config);
        };
    }

    @Override
    public String getChannelType() {
        return "sms";
    }

    @Override
    public NotificationResult send(Notification notification) {
        if (!canHandle(notification)) {
            throw new IllegalArgumentException(
                    String.format("SmsChannel cannot handle notification type: %s", 
                            notification.getChannelType())
            );
        }

        SmsNotification smsNotification = (SmsNotification) notification;

        ValidationResult validationResult = PhoneValidator.validate(smsNotification);
        if (validationResult.isInvalid()) {
            logger.warn("SMS validation failed: {}", validationResult.getErrorMessage().orElse("Unknown error"));
            return NotificationResult.failure(
                    validationResult.getErrorMessage().orElse("SMS validation failed"),
                    "VALIDATION_ERROR",
                    getChannelType(),
                    config.getProvider().getId()
            );
        }

        try {
            logger.debug("Sending SMS notification via provider: {}", provider.getProviderName());
            return provider.send(smsNotification);
        } catch (Exception e) {
            logger.error("Error sending SMS notification", e);
            return NotificationResult.failure(
                    String.format("Failed to send SMS: %s", e.getMessage()),
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
