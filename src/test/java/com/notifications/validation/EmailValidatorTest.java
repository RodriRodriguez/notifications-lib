package com.notifications.validation;

import com.notifications.channels.email.EmailNotification;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailValidatorTest {
    
    @Test
    void testValidEmailAddress() {
        assertTrue(EmailValidator.isValidEmail("test@example.com"));
        assertTrue(EmailValidator.isValidEmail("user.name@example.co.uk"));
        assertTrue(EmailValidator.isValidEmail("user+tag@example.com"));
    }

    @Test
    void testInvalidEmailAddress() {
        assertFalse(EmailValidator.isValidEmail("invalid-email"));
        assertFalse(EmailValidator.isValidEmail("@example.com"));
        assertFalse(EmailValidator.isValidEmail("test@"));
        assertFalse(EmailValidator.isValidEmail(""));
        assertFalse(EmailValidator.isValidEmail(null));
    }

    @Test
    void testValidateValidEmailNotification() {
        EmailNotification email = EmailNotification.builder()
                .to("recipient@example.com")
                .subject("Test Subject")
                .body("Test Body")
                .build();

        ValidationResult result = EmailValidator.validate(email);

        assertTrue(result.isValid());
        assertTrue(result.getErrorMessage().isEmpty());
    }

    @Test
    void testValidateInvalidEmailNotification() {
        EmailNotification email = EmailNotification.builder()
                .to("invalid-email")
                .subject("Test Subject")
                .body("Test Body")
                .build();

        ValidationResult result = EmailValidator.validate(email);

        assertTrue(result.isInvalid());
        assertTrue(result.getErrorMessage().isPresent());
    }

    @Test
    void testValidateNullNotification() {
        ValidationResult result = EmailValidator.validate(null);

        assertTrue(result.isInvalid());
        assertTrue(result.getErrorMessage().isPresent());
    }
}
