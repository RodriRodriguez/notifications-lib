package com.notifications.model;

import java.util.Objects;

public final class NotificationContent {
    
    private final String subject;
    private final String body;
    private final boolean html;

    public NotificationContent(String subject, String body) {
        this(subject, body, false);
    }

    public NotificationContent(String subject, String body, boolean html) {
        this.subject = Objects.requireNonNull(subject, "Subject cannot be null");
        this.body = Objects.requireNonNull(body, "Body cannot be null");
        this.html = html;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public boolean isHtml() {
        return html;
    }

    public static NotificationContent html(String subject, String htmlBody) {
        return new NotificationContent(subject, htmlBody, true);
    }

    public static NotificationContent text(String subject, String body) {
        return new NotificationContent(subject, body, false);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationContent that = (NotificationContent) o;
        return html == that.html &&
                Objects.equals(subject, that.subject) &&
                Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, body, html);
    }

    @Override
    public String toString() {
        return String.format("NotificationContent{subject='%s', html=%s, bodyLength=%d}",
                subject, html, body.length());
    }
}
