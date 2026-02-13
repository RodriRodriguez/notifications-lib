package com.notifications.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotificationResultTest {
    
    @Test
    void testSuccessResult() {
        NotificationResult result = NotificationResult.success("msg-123", "email", "sendgrid");
        
        assertTrue(result.isSuccess());
        assertFalse(result.isFailure());
        assertEquals("msg-123", result.getMessageId().orElse(null));
        assertTrue(result.getError().isEmpty());
        assertTrue(result.getErrorCode().isEmpty());
        assertEquals("email", result.getChannel());
        assertEquals("sendgrid", result.getProvider());
    }

    @Test
    void testFailureResult() {
        NotificationResult result = NotificationResult.failure("Network error", "NETWORK_ERROR", "sms", "twilio");
        
        assertFalse(result.isSuccess());
        assertTrue(result.isFailure());
        assertTrue(result.getMessageId().isEmpty());
        assertEquals("Network error", result.getError().orElse(null));
        assertEquals("NETWORK_ERROR", result.getErrorCode().orElse(null));
        assertEquals("sms", result.getChannel());
        assertEquals("twilio", result.getProvider());
    }

    @Test
    void testBuilder() {
        NotificationResult result = new NotificationResult.Builder()
                .success(true)
                .messageId("test-id")
                .channel("push")
                .provider("firebase")
                .build();
        
        assertTrue(result.isSuccess());
        assertEquals("test-id", result.getMessageId().orElse(null));
        assertEquals("push", result.getChannel());
        assertEquals("firebase", result.getProvider());
    }
}
