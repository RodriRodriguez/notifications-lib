package com.notifications.providers.email.dto;

import java.util.HashMap;
import java.util.Map;

public final class SendGridResponse {
    
    private final int statusCode;
    private final Map<String, String> headers;
    private final String body;

    public SendGridResponse(int statusCode, Map<String, String> headers, String body) {
        this.statusCode = statusCode;
        this.headers = headers != null ? Map.copyOf(headers) : Map.of();
        this.body = body;
    }

    public static SendGridResponse success(String messageId) {
        Map<String, String> headers = new HashMap<>();
        headers.put("x-message-id", messageId);
        headers.put("date", java.time.Instant.now().toString());
        headers.put("connection", "keep-alive");
        
        return new SendGridResponse(202, headers, "");
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public String getMessageId() {
        return headers.get("x-message-id");
    }
}
