package com.notifications.core;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class NotificationResult {
    
    private final boolean success;
    private final String messageId;
    private final String error;
    private final String errorCode;
    private final Instant timestamp;
    private final String channel;
    private final String provider;
    private final int statusCode;
    private final Map<String, Object> providerMetadata;

    private NotificationResult(Builder builder) {
        this.success = builder.success;
        this.messageId = builder.messageId;
        this.error = builder.error;
        this.errorCode = builder.errorCode;
        this.timestamp = builder.timestamp != null ? builder.timestamp : Instant.now();
        this.channel = builder.channel;
        this.provider = builder.provider;
        this.statusCode = builder.statusCode != null ? builder.statusCode : (success ? 200 : 500);
        this.providerMetadata = builder.providerMetadata != null 
            ? Collections.unmodifiableMap(new HashMap<>(builder.providerMetadata)) 
            : Map.of();
    }

    public static NotificationResult success(String messageId, String channel, String provider) {
        return new Builder()
                .success(true)
                .messageId(messageId)
                .channel(channel)
                .provider(provider)
                .statusCode(202)
                .build();
    }

    public static NotificationResult failure(String error, String errorCode, String channel, String provider) {
        return new Builder()
                .success(false)
                .error(error)
                .errorCode(errorCode)
                .channel(channel)
                .provider(provider)
                .statusCode(400)
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

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, Object> getProviderMetadata() {
        return providerMetadata;
    }

    @Override
    public String toString() {
        if (success) {
            return String.format("NotificationResult{success=true, messageId='%s', channel='%s', provider='%s', statusCode=%d, timestamp=%s}",
                    messageId, channel, provider, statusCode, timestamp);
        } else {
            return String.format("NotificationResult{success=false, error='%s', errorCode='%s', channel='%s', provider='%s', statusCode=%d, timestamp=%s}",
                    error, errorCode, channel, provider, statusCode, timestamp);
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
        private Integer statusCode;
        private Map<String, Object> providerMetadata;

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

        public Builder statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder providerMetadata(Map<String, Object> providerMetadata) {
            this.providerMetadata = providerMetadata != null ? new HashMap<>(providerMetadata) : null;
            return this;
        }

        public Builder addMetadata(String key, Object value) {
            if (this.providerMetadata == null) {
                this.providerMetadata = new HashMap<>();
            }
            this.providerMetadata.put(key, value);
            return this;
        }

        public NotificationResult build() {
            return new NotificationResult(this);
        }
    }
}
