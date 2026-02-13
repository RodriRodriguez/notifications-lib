package com.notifications.validation;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.notifications.channels.sms.SmsNotification;
import com.notifications.model.Recipient;

public final class PhoneValidator {

    private static final PhoneNumberUtil PHONE_UTIL = PhoneNumberUtil.getInstance();

    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        
        try {
            Phonenumber.PhoneNumber parsedNumber = PHONE_UTIL.parse(
                    phoneNumber.trim(), 
                    null
            );

            return PHONE_UTIL.isValidNumber(parsedNumber) || 
                   PHONE_UTIL.isPossibleNumber(parsedNumber);
        } catch (NumberParseException e) {
            return false;
        }
    }

    public static PhoneValidationResult validateDetailed(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return PhoneValidationResult.invalid("Phone number cannot be null or empty");
        }
        
        try {
            Phonenumber.PhoneNumber parsedNumber = PHONE_UTIL.parse(
                    phoneNumber.trim(), 
                    null
            );

            if (PHONE_UTIL.isValidNumber(parsedNumber)) {
                return PhoneValidationResult.valid(parsedNumber);
            } else if (PHONE_UTIL.isPossibleNumber(parsedNumber)) {
                return PhoneValidationResult.valid(parsedNumber);
            } else {
                return PhoneValidationResult.invalid(
                        String.format("Invalid phone number format: %s", phoneNumber)
                );
            }
        } catch (NumberParseException e) {
            return PhoneValidationResult.invalid(
                    String.format("Failed to parse phone number '%s': %s", 
                            phoneNumber, e.getMessage())
            );
        }
    }

    public static ValidationResult validate(SmsNotification notification) {
        if (notification == null) {
            return ValidationResult.failure("Notification cannot be null");
        }

        if (notification.getRecipients().isEmpty()) {
            return ValidationResult.failure("At least one recipient is required");
        }

        for (Recipient recipient : notification.getRecipients()) {
            String phoneNumber = recipient.getAddress();
            if (!isValidPhoneNumber(phoneNumber)) {
                PhoneValidationResult detailedResult = validateDetailed(phoneNumber);
                String errorMessage = detailedResult.isValid() 
                    ? String.format("Invalid phone number: %s. Expected E.164 format (e.g., +1234567890)", phoneNumber)
                    : detailedResult.getErrorMessage().orElse(
                        String.format("Invalid phone number: %s. Expected E.164 format (e.g., +1234567890)", phoneNumber)
                    );
                
                return ValidationResult.failure(errorMessage);
            }
        }

        if (notification.getContent() == null) {
            return ValidationResult.failure("Content cannot be null");
        }

        String body = notification.getContent().getBody();
        if (body == null || body.trim().isEmpty()) {
            return ValidationResult.failure("SMS message body cannot be empty");
        }

        if (body.length() > 1600) {
            return ValidationResult.failure(
                    String.format("SMS message too long: %d characters. Maximum is 1600.", body.length())
            );
        }

        return ValidationResult.success();
    }
}
