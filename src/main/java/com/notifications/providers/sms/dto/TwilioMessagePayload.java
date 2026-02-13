package com.notifications.providers.sms.dto;

public final class TwilioMessagePayload {
    
    private final String from;
    private final String to;
    private final String body;
    private final String statusCallback;

    public TwilioMessagePayload(String from, String to, String body, String statusCallback) {
        this.from = from;
        this.to = to;
        this.body = body;
        this.statusCallback = statusCallback;
    }

    public TwilioMessagePayload(String from, String to, String body) {
        this(from, to, body, null);
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getBody() {
        return body;
    }

    public String getStatusCallback() {
        return statusCallback;
    }
}
