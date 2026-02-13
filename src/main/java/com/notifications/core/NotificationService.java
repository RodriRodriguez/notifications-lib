package com.notifications.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public final class NotificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    
    private final List<NotificationChannel> channels;

    private NotificationService(Builder builder) {
        this.channels = new CopyOnWriteArrayList<>(builder.channels);
        logger.info("NotificationService initialized with {} channel(s)", channels.size());
    }

    public static Builder builder() {
        return new Builder();
    }

    public NotificationResult send(Notification notification) {
        Objects.requireNonNull(notification, "Notification cannot be null");
        
        logger.debug("Attempting to send notification: {}", notification);
        
        if (!notification.isValid()) {
            logger.warn("Invalid notification: {}", notification);
            return NotificationResult.failure(
                    "Notification validation failed",
                    "VALIDATION_ERROR",
                    notification.getChannelType(),
                    "unknown"
            );
        }

        NotificationChannel channel = findChannel(notification);
        if (channel == null) {
            logger.error("No channel found for notification type: {}", notification.getChannelType());
            return NotificationResult.failure(
                    String.format("No channel configured for type: %s", notification.getChannelType()),
                    "CHANNEL_NOT_FOUND",
                    notification.getChannelType(),
                    "unknown"
            );
        }

        if (!channel.isReady()) {
            logger.warn("Channel {} is not ready", channel.getChannelType());
            return NotificationResult.failure(
                    String.format("Channel %s is not ready", channel.getChannelType()),
                    "CHANNEL_NOT_READY",
                    notification.getChannelType(),
                    "unknown"
            );
        }

        try {
            logger.info("Sending notification via channel: {}", channel.getChannelType());
            NotificationResult result = channel.send(notification);
            
            if (result.isSuccess()) {
                logger.info("Notification sent successfully. Message ID: {}", result.getMessageId().orElse("unknown"));
            } else {
                logger.warn("Notification failed: {}", result.getError().orElse("Unknown error"));
            }
            
            return result;
        } catch (Exception e) {
            logger.error("Unexpected error sending notification", e);
            return NotificationResult.failure(
                    String.format("Unexpected error: %s", e.getMessage()),
                    "UNEXPECTED_ERROR",
                    notification.getChannelType(),
                    "unknown"
            );
        }
    }

    private NotificationChannel findChannel(Notification notification) {
        return channels.stream()
                .filter(channel -> channel.canHandle(notification))
                .findFirst()
                .orElse(null);
    }

    public List<NotificationChannel> getChannels() {
        return Collections.unmodifiableList(channels);
    }

    public int getChannelCount() {
        return channels.size();
    }

    public static class Builder {
        private final List<NotificationChannel> channels = new ArrayList<>();

        public Builder addChannel(NotificationChannel channel) {
            Objects.requireNonNull(channel, "Channel cannot be null");
            this.channels.add(channel);
            return this;
        }

        public Builder addChannels(List<NotificationChannel> channels) {
            Objects.requireNonNull(channels, "Channels cannot be null");
            channels.forEach(this::addChannel);
            return this;
        }

        public NotificationService build() {
            if (channels.isEmpty()) {
                throw new IllegalStateException("At least one channel must be configured");
            }
            return new NotificationService(this);
        }
    }
}
