package com.notifications.core;

import java.time.Instant;
import java.util.Optional;

public final class NotificationResult {
    
    private final boolean success;
    private final String messageId;
    private final String error;
    private final String errorCode;
    private final Instant timestamp;
    private final String channel;
    private final String provider;

    private NotificationResult(Builder builder) {
        this.success = builder.success;
        this.messageId = builder.messageId;
        this.error = builder.error;
        this.errorCode = builder.errorCode;
        this.timestamp = builder.timestamp != null ? builder.timestamp : Instant.now();
        this.channel = builder.channel;
        this.provider = builder.provider;
    }

    public static NotificationResult success(String messageId, String channel, String provider) {
        return new Builder()
                .success(true)
                .messageId(messageId)
                .channel(channel)
                .provider(provider)
                .build();
    }

    public static NotificationResult failure(String error, String errorCode, String channel, String provider) {
        return new Builder()
                .success(false)
                .error(error)
                .errorCode(errorCode)
                .channel(channel)
                .provider(provider)
                .build();
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isFailure() {
        return !success;
    }

    public Optional<String> getMessageId() {
        return Optional.ofNullable(messageId);
    }

    public Optional<String> getError() {
        return Optional.ofNullable(error);
    }

    public Optional<String> getErrorCode() {
        return Optional.ofNullable(errorCode);
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getChannel() {
        return channel;
    }

    public String getProvider() {
        return provider;
    }

    @Override
    public String toString() {
        if (success) {
            return String.format("NotificationResult{success=true, messageId='%s', channel='%s', provider='%s', timestamp=%s}",
                    messageId, channel, provider, timestamp);
        } else {
            return String.format("NotificationResult{success=false, error='%s', errorCode='%s', channel='%s', provider='%s', timestamp=%s}",
                    error, errorCode, channel, provider, timestamp);
        }
    }

    public static class Builder {
        private boolean success;
        private String messageId;
        private String error;
        private String errorCode;
        private Instant timestamp;
        private String channel;
        private String provider;

        public Builder success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder messageId(String messageId) {
            this.messageId = messageId;
            return this;
        }

        public Builder error(String error) {
            this.error = error;
            return this;
        }

        public Builder errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public Builder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder channel(String channel) {
            this.channel = channel;
            return this;
        }

        public Builder provider(String provider) {
            this.provider = provider;
            return this;
        }

        public NotificationResult build() {
            return new NotificationResult(this);
        }
    }
}
