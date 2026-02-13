package com.notifications.providers.email.dto;

import java.util.ArrayList;
import java.util.List;

public final class SendGridMailPayload {
    
    private final List<SendGridPersonalization> personalizations;
    private final SendGridEmailAddress from;
    private final List<SendGridContentBlock> content;

    public SendGridMailPayload(List<SendGridPersonalization> personalizations, 
                               SendGridEmailAddress from,
                               List<SendGridContentBlock> content) {
        this.personalizations = personalizations != null ? new ArrayList<>(personalizations) : new ArrayList<>();
        this.from = from;
        this.content = content != null ? new ArrayList<>(content) : new ArrayList<>();
    }

    public List<SendGridPersonalization> getPersonalizations() {
        return personalizations;
    }

    public SendGridEmailAddress getFrom() {
        return from;
    }

    public List<SendGridContentBlock> getContent() {
        return content;
    }
}
