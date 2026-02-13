package com.notifications.providers.sms;

import com.notifications.channels.sms.SmsConfig;
import com.notifications.channels.sms.SmsNotification;
import com.notifications.core.NotificationProvider;
import com.notifications.core.NotificationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public final class TwilioProvider implements NotificationProvider<SmsNotification> {
    
    private static final Logger logger = LoggerFactory.getLogger(TwilioProvider.class);
    
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
        logger.debug("  From: {}", config.getFromPhoneNumber());
        logger.debug("  To: {}", notification.getRecipients());
        logger.debug("  Body: {}", notification.getMessage());

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String messageId = "tw-" + UUID.randomUUID().toString().substring(0, 8);
        logger.info("Twilio SMS sent successfully. Message ID: {}", messageId);
        
        return NotificationResult.success(messageId, getChannelType(), getProviderName());
    }

    @Override
    public boolean isConfigured() {
        return config != null && 
               config.getAccountSid() != null && !config.getAccountSid().trim().isEmpty() &&
               config.getAuthToken() != null && !config.getAuthToken().trim().isEmpty();
    }

    private String maskAccountSid(String accountSid) {
        if (accountSid == null || accountSid.length() < 8) {
            return "***";
        }
        return accountSid.substring(0, 4) + "..." + accountSid.substring(accountSid.length() - 4);
    }
}
