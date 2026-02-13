package com.notifications.channels.email;

import com.notifications.core.Notification;
import com.notifications.model.NotificationContent;
import com.notifications.model.NotificationMetadata;
import com.notifications.model.Recipient;
import lombok.Builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class EmailNotification extends Notification {
    
    private final List<String> ccRecipients;
    private final List<String> bccRecipients;

    @Builder
    private EmailNotification(List<Recipient> recipients, NotificationContent content,
                             NotificationMetadata metadata, List<String> ccRecipients,
                             List<String> bccRecipients) {
        super(recipients != null ? recipients : new ArrayList<>(), content, metadata);
        this.ccRecipients = ccRecipients != null ? new ArrayList<>(ccRecipients) : new ArrayList<>();
        this.bccRecipients = bccRecipients != null ? new ArrayList<>(bccRecipients) : new ArrayList<>();
    }

    public static EmailNotificationBuilder builder() {
        return new EmailNotificationBuilder();
    }

    public static EmailNotification of(String to, String subject, String body) {
        return EmailNotification.builder()
                .to(to)
                .subject(subject)
                .body(body)
                .build();
    }

    public List<String> getCcRecipients() {
        return Collections.unmodifiableList(ccRecipients);
    }

    public List<String> getBccRecipients() {
        return Collections.unmodifiableList(bccRecipients);
    }

    @Override
    public String getChannelType() {
        return "email";
    }

    @Override
    public boolean isValid() {
        return super.isValid() && 
               getContent().getSubject() != null && 
               !getContent().getSubject().trim().isEmpty() &&
               getContent().getBody() != null && 
               !getContent().getBody().trim().isEmpty();
    }

    public static class EmailNotificationBuilder {
        private List<Recipient> recipients;
        private NotificationContent content;
        private NotificationMetadata metadata;
        private List<String> ccRecipients;
        private List<String> bccRecipients;

        public EmailNotificationBuilder recipient(String email) {
            if (this.recipients == null) {
                this.recipients = new ArrayList<>();
            }
            this.recipients.add(new Recipient(email));
            return this;
        }

        public EmailNotificationBuilder recipientsFromEmails(List<String> emails) {
            if (this.recipients == null) {
                this.recipients = new ArrayList<>();
            }
            this.recipients.addAll(emails.stream()
                    .map(Recipient::new)
                    .collect(Collectors.toList()));
            return this;
        }

        public EmailNotificationBuilder to(String email) {
            return recipient(email);
        }

        public EmailNotificationBuilder subject(String subject) {
            if (this.content == null) {
                this.content = NotificationContent.text(subject, "");
            } else {
                this.content = new NotificationContent(subject, this.content.getBody(), this.content.isHtml());
            }
            return this;
        }

        public EmailNotificationBuilder body(String body) {
            if (this.content == null) {
                this.content = NotificationContent.text("", body);
            } else {
                this.content = new NotificationContent(this.content.getSubject(), body, this.content.isHtml());
            }
            return this;
        }

        public EmailNotificationBuilder htmlBody(String htmlBody) {
            if (this.content == null) {
                this.content = NotificationContent.html("", htmlBody);
            } else {
                this.content = new NotificationContent(this.content.getSubject(), htmlBody, true);
            }
            return this;
        }

        public EmailNotificationBuilder cc(String email) {
            if (this.ccRecipients == null) {
                this.ccRecipients = new ArrayList<>();
            }
            this.ccRecipients.add(email);
            return this;
        }

        public EmailNotificationBuilder bcc(String email) {
            if (this.bccRecipients == null) {
                this.bccRecipients = new ArrayList<>();
            }
            this.bccRecipients.add(email);
            return this;
        }

        public EmailNotification build() {
            return new EmailNotification(recipients, content, metadata, ccRecipients, bccRecipients);
        }
    }
}
