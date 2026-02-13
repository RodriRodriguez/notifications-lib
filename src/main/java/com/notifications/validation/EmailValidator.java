package com.notifications.validation;

import com.notifications.channels.email.EmailNotification;
import com.notifications.model.Recipient;

import java.util.regex.Pattern;

public final class EmailValidator {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static ValidationResult validate(EmailNotification notification) {
        if (notification == null) {
            return ValidationResult.failure("Notification cannot be null");
        }

        if (notification.getRecipients().isEmpty()) {
            return ValidationResult.failure("At least one recipient is required");
        }
        for (Recipient recipient : notification.getRecipients()) {
            String address = recipient.getAddress();
            if (address == null || address.trim().isEmpty()) {
                return ValidationResult.failure("Invalid email address: email cannot be empty");
            }
            if (!isValidEmail(address)) {
                return ValidationResult.failure(
                        String.format("Invalid email address: %s", address)
                );
            }
        }

        if (notification.getCcRecipients() != null) {
            for (String cc : notification.getCcRecipients()) {
                if (cc == null || cc.trim().isEmpty()) {
                    return ValidationResult.failure("Invalid CC email address: email cannot be empty");
                }
                if (!isValidEmail(cc)) {
                    return ValidationResult.failure(
                            String.format("Invalid CC email address: %s", cc)
                    );
                }
            }
        }

        if (notification.getBccRecipients() != null) {
            for (String bcc : notification.getBccRecipients()) {
                if (!isValidEmail(bcc)) {
                    return ValidationResult.failure(
                            String.format("Invalid BCC email address: %s", bcc)
                    );
                }
            }
        }

        if (notification.getContent() == null) {
            return ValidationResult.failure("Content cannot be null");
        }

        if (notification.getContent().getSubject() == null || 
            notification.getContent().getSubject().trim().isEmpty()) {
            return ValidationResult.failure("Subject cannot be empty");
        }

        if (notification.getContent().getBody() == null || 
            notification.getContent().getBody().trim().isEmpty()) {
            return ValidationResult.failure("Body cannot be empty");
        }

        return ValidationResult.success();
    }
}
