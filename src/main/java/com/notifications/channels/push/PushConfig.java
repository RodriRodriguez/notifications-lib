package com.notifications.channels.push;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder
public final class PushConfig {
    public enum PushProvider {
        FIREBASE("firebase", "Firebase Cloud Messaging");

        private final String id;
        private final String name;

        PushProvider(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    private final PushProvider provider;
    private final String apiKey;
    private final String appId;
    private final String projectId;
    private final String serviceAccountJson;

    public PushConfig(PushProvider provider, String apiKey, String appId,
                     String projectId, String serviceAccountJson) {
        this.provider = Objects.requireNonNull(provider, "Provider cannot be null");
        this.apiKey = apiKey;
        this.appId = appId;
        this.projectId = projectId;
        this.serviceAccountJson = serviceAccountJson;
    }

    public boolean isValid() {
        return provider != null;
    }
}
