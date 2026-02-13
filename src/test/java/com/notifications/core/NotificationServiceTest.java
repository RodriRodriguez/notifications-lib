package com.notifications.core;

import com.notifications.channels.email.EmailChannel;
import com.notifications.channels.email.EmailConfig;
import com.notifications.channels.email.EmailNotification;
import com.notifications.channels.sms.SmsChannel;
import com.notifications.channels.sms.SmsConfig;
import com.notifications.channels.sms.SmsNotification;
import com.notifications.model.NotificationContent;
import com.notifications.model.Recipient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NotificationServiceTest {
    
    private NotificationService service;

    @BeforeEach
    void setUp() {
        EmailConfig emailConfig = EmailConfig.builder()
                .provider(EmailConfig.EmailProvider.SENDGRID)
                .apiKey("test-key")
                .fromEmail("test@example.com")
                .build();

        SmsConfig smsConfig = SmsConfig.builder()
                .provider(SmsConfig.SmsProvider.TWILIO)
                .accountSid("test-sid")
                .authToken("test-token")
                .fromPhoneNumber("+12025551234")
                .build();

        service = NotificationService.builder()
                .addChannel(new EmailChannel(emailConfig))
                .addChannel(new SmsChannel(smsConfig))
                .build();
    }

    @Test
    void testSendEmailNotification() {
        EmailNotification email = EmailNotification.builder()
                .to("recipient@example.com")
                .subject("Test Subject")
                .body("Test Body")
                .build();

        NotificationResult result = service.send(email);

        assertTrue(result.isSuccess());
        assertTrue(result.getMessageId().isPresent());
        assertEquals("email", result.getChannel());
    }

    @Test
    void testSendSmsNotification() {
        SmsNotification sms = SmsNotification.builder()
                .to("+12025551234")
                .message("Test message")
                .build();

        NotificationResult result = service.send(sms);

        assertTrue(result.isSuccess());
        assertTrue(result.getMessageId().isPresent());
        assertEquals("sms", result.getChannel());
    }

    @Test
    void testInvalidNotification() {
        EmailNotification invalidEmail = EmailNotification.builder()
                .to("invalid-email")
                .subject("")
                .body("")
                .build();

        NotificationResult result = service.send(invalidEmail);

        assertTrue(result.isFailure());
        assertTrue(result.getError().isPresent());
        assertEquals("VALIDATION_ERROR", result.getErrorCode().orElse(null));
    }

    @Test
    void testNullNotification() {
        assertThrows(NullPointerException.class, () -> {
            service.send(null);
        });
    }

    @Test
    void testGetChannels() {
        assertEquals(2, service.getChannelCount());
        assertEquals(2, service.getChannels().size());
    }
}
