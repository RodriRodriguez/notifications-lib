package com.notifications.providers.push.dto;

import java.util.Map;

public final class FcmMessagePayload {
    
    private final FcmMessage message;

    public FcmMessagePayload(FcmMessage message) {
        this.message = message;
    }

    public FcmMessage getMessage() {
        return message;
    }

    public static final class FcmMessage {
        private final String token;
        private final FcmNotification notification;
        private final Map<String, String> data;

        public FcmMessage(String token, FcmNotification notification, Map<String, String> data) {
            this.token = token;
            this.notification = notification;
            this.data = data;
        }

        public String getToken() {
            return token;
        }

        public FcmNotification getNotification() {
            return notification;
        }

        public Map<String, String> getData() {
            return data;
        }
    }

    public static final class FcmNotification {
        private final String title;
        private final String body;

        public FcmNotification(String title, String body) {
            this.title = title;
            this.body = body;
        }

        public String getTitle() {
            return title;
        }

        public String getBody() {
            return body;
        }
    }
}
