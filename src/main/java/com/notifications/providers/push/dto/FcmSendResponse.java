package com.notifications.providers.push.dto;

public final class FcmSendResponse {
    
    private final String name;
    private final int statusCode;

    public FcmSendResponse(String name, int statusCode) {
        this.name = name;
        this.statusCode = statusCode;
    }

    public static FcmSendResponse success(String projectId, String messageId) {
        String name = String.format("projects/%s/messages/%s", projectId, messageId);
        return new FcmSendResponse(name, 200);
    }

    public String getName() {
        return name;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessageId() {
        if (name == null || !name.contains("/messages/")) {
            return null;
        }
        return name.substring(name.lastIndexOf("/messages/") + "/messages/".length());
    }
}
