package com.notifications.providers.email.dto;

public final class SendGridEmailAddress {
    
    private final String email;
    private final String name;

    public SendGridEmailAddress(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public SendGridEmailAddress(String email) {
        this(email, null);
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
