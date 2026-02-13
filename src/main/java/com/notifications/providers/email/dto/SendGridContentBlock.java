package com.notifications.providers.email.dto;

public final class SendGridContentBlock {
    
    private final String type;
    private final String value;

    public SendGridContentBlock(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static SendGridContentBlock plainText(String text) {
        return new SendGridContentBlock("text/plain", text);
    }

    public static SendGridContentBlock html(String html) {
        return new SendGridContentBlock("text/html", html);
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
