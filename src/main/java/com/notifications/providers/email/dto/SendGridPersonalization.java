package com.notifications.providers.email.dto;

import java.util.ArrayList;
import java.util.List;

public final class SendGridPersonalization {
    
    private final List<SendGridEmailAddress> to;
    private final List<SendGridEmailAddress> cc;
    private final String subject;

    public SendGridPersonalization(List<SendGridEmailAddress> to, 
                                   List<SendGridEmailAddress> cc,
                                   String subject) {
        this.to = to != null ? new ArrayList<>(to) : new ArrayList<>();
        this.cc = cc != null ? new ArrayList<>(cc) : new ArrayList<>();
        this.subject = subject;
    }

    public List<SendGridEmailAddress> getTo() {
        return to;
    }

    public List<SendGridEmailAddress> getCc() {
        return cc;
    }

    public String getSubject() {
        return subject;
    }
}
