package com.notifications.providers.sms.dto;

import java.time.Instant;

public final class TwilioMessageResponse {
    
    private final String sid;
    private final String status;
    private final String dateCreated;
    private final int statusCode;

    public TwilioMessageResponse(String sid, String status, String dateCreated, int statusCode) {
        this.sid = sid;
        this.status = status;
        this.dateCreated = dateCreated;
        this.statusCode = statusCode;
    }

    public static TwilioMessageResponse success(String sid) {
        return new TwilioMessageResponse(
                sid,
                "queued",
                Instant.now().toString(),
                201
        );
    }

    public String getSid() {
        return sid;
    }

    public String getStatus() {
        return status;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
