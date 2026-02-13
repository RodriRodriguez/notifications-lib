package com.notifications.channels.sms;

import com.notifications.core.Notification;
import com.notifications.model.NotificationContent;
import com.notifications.model.NotificationMetadata;
import com.notifications.model.Recipient;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class SmsNotification extends Notification {
    
    @Builder
    private SmsNotification(List<Recipient> recipients, NotificationContent content,
                           NotificationMetadata metadata) {
        super(recipients != null ? recipients : new ArrayList<>(), content, metadata);
    }

    public static SmsNotificationBuilder builder() {
        return new SmsNotificationBuilder();
    }

    public static SmsNotification of(String to, String message) {
        return SmsNotification.builder()
                .to(to)
                .message(message)
                .build();
    }

    @Override
    public String getChannelType() {
        return "sms";
    }

    @Override
    public boolean isValid() {
        return getRecipients() != null && !getRecipients().isEmpty() 
                && getContent() != null 
                && getContent().getBody() != null && !getContent().getBody().trim().isEmpty();
    }

    public String getMessage() {
        return getContent() != null ? getContent().getBody() : "";
    }

    public static class SmsNotificationBuilder {
        private List<Recipient> recipients;
        private NotificationContent content;
        private NotificationMetadata metadata;

        public SmsNotificationBuilder to(String phoneNumber) {
            if (this.recipients == null) {
                this.recipients = new ArrayList<>();
            }
            this.recipients.add(new Recipient(phoneNumber));
            return this;
        }

        public SmsNotificationBuilder recipientsFromSms(List<String> phoneNumbers) {
            if (this.recipients == null) {
                this.recipients = new ArrayList<>();
            }
            this.recipients.addAll(phoneNumbers.stream()
                    .map(Recipient::new)
                    .collect(Collectors.toList()));
            return this;
        }

        public SmsNotificationBuilder message(String message) {
            this.content = NotificationContent.text("", message);
            return this;
        }

        public SmsNotification build() {
            return new SmsNotification(recipients, content, metadata);
        }
    }
}

