package com.notifications.channels.email;

import com.notifications.core.NotificationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailChannelTest {
    
    private EmailChannel emailChannel;

    @BeforeEach
    void setUp() {
        EmailConfig config = EmailConfig.builder()
                .provider(EmailConfig.EmailProvider.SENDGRID)
                .apiKey("test-api-key")
                .fromEmail("sender@example.com")
                .fromName("Test Sender")
                .build();

        emailChannel = new EmailChannel(config);
    }

    @Test
    void testGetChannelType() {
        assertEquals("email", emailChannel.getChannelType());
    }

    @Test
    void testSendValidEmail() {
        EmailNotification email = EmailNotification.builder()
                .to("recipient@example.com")
                .subject("Test Subject")
                .body("Test Body")
                .build();

        NotificationResult result = emailChannel.send(email);

        assertTrue(result.isSuccess());
        assertTrue(result.getMessageId().isPresent());
        assertEquals("email", result.getChannel());
        assertEquals("sendgrid", result.getProvider());
    }

    @Test
    void testSendEmailWithInvalidRecipient() {
        EmailNotification email = EmailNotification.builder()
                .to("invalid-email")
                .subject("Test Subject")
                .body("Test Body")
                .build();

        NotificationResult result = emailChannel.send(email);

        assertTrue(result.isFailure());
        assertEquals("VALIDATION_ERROR", result.getErrorCode().orElse(null));
    }

    @Test
    void testCanHandle() {
        EmailNotification email = EmailNotification.builder()
                .to("test@example.com")
                .subject("Test")
                .body("Test")
                .build();

        assertTrue(emailChannel.canHandle(email));
    }

    @Test
    void testIsReady() {
        assertTrue(emailChannel.isReady());
    }
}
