package com.notifications.channels.push;

import com.notifications.core.Notification;
import com.notifications.model.NotificationContent;
import com.notifications.model.NotificationMetadata;
import com.notifications.model.Recipient;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

public final class PushNotification extends Notification {
    
    @Builder
    private PushNotification(List<Recipient> recipients, NotificationContent content,
                            NotificationMetadata metadata) {
        super(recipients != null ? recipients : new ArrayList<>(), content, metadata);
    }

    public static PushNotificationBuilder builder() {
        return new PushNotificationBuilder();
    }

    public static PushNotification of(String deviceToken, String title, String body) {
        return PushNotification.builder()
                .recipients(List.of(new Recipient(deviceToken)))
                .content(NotificationContent.text(title, body))
                .build();
    }

    @Override
    public String getChannelType() {
        return "push";
    }

    public String getTitle() {
        return getContent() != null ? getContent().getSubject() : "";
    }

    public String getBody() {
        return getContent() != null ? getContent().getBody() : "";
    }

    public static class PushNotificationBuilder {
        private List<Recipient> recipients;
        private NotificationContent content;
        private NotificationMetadata metadata;

        public PushNotificationBuilder to(String deviceToken) {
            if (this.recipients == null) {
                this.recipients = new ArrayList<>();
            }
            this.recipients.add(new Recipient(deviceToken));
            return this;
        }

        public PushNotificationBuilder title(String title) {
            if (this.content == null) {
                this.content = NotificationContent.text(title, "");
            } else {
                this.content = new NotificationContent(title, this.content.getBody(), this.content.isHtml());
            }
            return this;
        }

        public PushNotificationBuilder body(String body) {
            if (this.content == null) {
                this.content = NotificationContent.text("", body);
            } else {
                this.content = new NotificationContent(this.content.getSubject(), body, this.content.isHtml());
            }
            return this;
        }

        public PushNotification build() {
            return new PushNotification(recipients, content, metadata);
        }
    }
}
