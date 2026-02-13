package com.notifications.core;

import com.notifications.model.NotificationContent;
import com.notifications.model.NotificationMetadata;
import com.notifications.model.Recipient;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Notification {
    
    private final List<Recipient> recipients;
    private final NotificationContent content;
    private final NotificationMetadata metadata;
    private final Instant createdAt;

    protected Notification(List<Recipient> recipients, NotificationContent content, NotificationMetadata metadata) {
        this.recipients = Objects.requireNonNull(recipients, "Recipients cannot be null");
        this.content = Objects.requireNonNull(content, "Content cannot be null");
        this.metadata = metadata != null ? metadata : NotificationMetadata.empty();
        this.createdAt = Instant.now();
        
        if (recipients.isEmpty()) {
            throw new IllegalArgumentException("At least one recipient is required");
        }
    }

    public List<Recipient> getRecipients() {
        return Collections.unmodifiableList(recipients);
    }

    public NotificationContent getContent() {
        return content;
    }

    public NotificationMetadata getMetadata() {
        return metadata;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public abstract String getChannelType();

    public boolean isValid() {
        return recipients != null && !recipients.isEmpty() 
                && content != null 
                && content.getSubject() != null && !content.getSubject().trim().isEmpty()
                && content.getBody() != null && !content.getBody().trim().isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(recipients, that.recipients) &&
                Objects.equals(content, that.content) &&
                Objects.equals(metadata, that.metadata) &&
                Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipients, content, metadata, createdAt);
    }

    @Override
    public String toString() {
        return String.format("Notification{channelType='%s', recipients=%d, createdAt=%s}",
                getChannelType(), recipients.size(), createdAt);
    }
}
