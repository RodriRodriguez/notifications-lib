package com.notifications.channels.email;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder
public final class EmailConfig {

    public enum EmailProvider {
        SENDGRID("sendgrid", "SendGrid");

        private final String id;
        private final String name;

        EmailProvider(String id, String name) {
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

    private final EmailProvider provider;
    private final String apiKey;
    private final String apiSecret;
    private final String fromEmail;
    private final String fromName;
    private final String replyTo;
    private final String region;

    public EmailConfig(EmailProvider provider, String apiKey, String apiSecret,
                      String fromEmail, String fromName, String replyTo, String region) {
        this.provider = Objects.requireNonNull(provider, "Provider cannot be null");
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.fromEmail = Objects.requireNonNull(fromEmail, "From email cannot be null");
        this.fromName = fromName;
        this.replyTo = replyTo;
        this.region = region;
    }

    public boolean isValid() {
        return provider != null && fromEmail != null && !fromEmail.trim().isEmpty();
    }
}
