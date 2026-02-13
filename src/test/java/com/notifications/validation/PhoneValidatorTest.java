package com.notifications.validation;

import com.notifications.channels.sms.SmsNotification;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhoneValidatorTest {
    
    @Test
    void testValidPhoneNumbers() {
        assertTrue(PhoneValidator.isValidPhoneNumber("+12025551234"));
        assertTrue(PhoneValidator.isValidPhoneNumber("+543513873741"));
        assertTrue(PhoneValidator.isValidPhoneNumber("+34612345678"));
        assertTrue(PhoneValidator.isValidPhoneNumber("+447911123456"));
    }

    @Test
    void testInvalidPhoneNumbers() {
        assertFalse(PhoneValidator.isValidPhoneNumber("1234567890"));
        assertFalse(PhoneValidator.isValidPhoneNumber("+123"));
        assertFalse(PhoneValidator.isValidPhoneNumber(""));
        assertFalse(PhoneValidator.isValidPhoneNumber(null));
        assertFalse(PhoneValidator.isValidPhoneNumber("invalid"));
    }

    @Test
    void testValidateValidSmsNotification() {
        SmsNotification sms = SmsNotification.builder()
                .to("+12025551234")
                .message("Test message")
                .build();

        ValidationResult result = PhoneValidator.validate(sms);

        assertTrue(result.isValid());
        assertTrue(result.getErrorMessage().isEmpty());
    }

    @Test
    void testValidateInvalidSmsNotification() {
        SmsNotification sms = SmsNotification.builder()
                .to("invalid-phone")
                .message("Test message")
                .build();

        ValidationResult result = PhoneValidator.validate(sms);

        assertTrue(result.isInvalid());
        assertTrue(result.getErrorMessage().isPresent());
    }

    @Test
    void testValidateDetailed() {
        PhoneValidationResult result = PhoneValidator.validateDetailed("+12025551234");
        
        assertTrue(result.isValid());
        assertTrue(result.getParsedNumber().isPresent());
        assertTrue(result.getErrorMessage().isEmpty());
    }

    @Test
    void testValidateDetailedInvalid() {
        PhoneValidationResult result = PhoneValidator.validateDetailed("invalid");
        
        assertFalse(result.isValid());
        assertTrue(result.getErrorMessage().isPresent());
    }
}
