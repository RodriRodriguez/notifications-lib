package com.notifications.channels.sms;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder
public final class SmsConfig {
    public enum SmsProvider {
        TWILIO("twilio", "Twilio");

        private final String id;
        private final String name;

        SmsProvider(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    private final SmsProvider provider;
    private final String accountSid;
    private final String authToken;
    private final String fromPhoneNumber;
    private final String accessKeyId;
    private final String secretAccessKey;
    private final String region;

    public SmsConfig(SmsProvider provider, String accountSid, String authToken,
                    String fromPhoneNumber, String accessKeyId, String secretAccessKey, String region) {
        this.provider = Objects.requireNonNull(provider, "Provider cannot be null");
        this.accountSid = accountSid;
        this.authToken = authToken;
        this.fromPhoneNumber = Objects.requireNonNull(fromPhoneNumber, "From phone number cannot be null");
        this.accessKeyId = accessKeyId;
        this.secretAccessKey = secretAccessKey;
        this.region = region;
    }

    public boolean isValid() {
        return provider != null && fromPhoneNumber != null && !fromPhoneNumber.trim().isEmpty();
    }
}
