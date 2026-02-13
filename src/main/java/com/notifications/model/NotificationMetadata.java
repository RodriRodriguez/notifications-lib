package com.notifications.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class NotificationMetadata {
    
    private final Map<String, String> properties;

    public NotificationMetadata() {
        this.properties = Collections.emptyMap();
    }

    public NotificationMetadata(Map<String, String> properties) {
        if (properties == null || properties.isEmpty()) {
            this.properties = Collections.emptyMap();
        } else {
            this.properties = Collections.unmodifiableMap(new HashMap<>(properties));
        }
    }

    public static NotificationMetadata empty() {
        return new NotificationMetadata();
    }

    public static NotificationMetadata of(Map<String, String> properties) {
        return new NotificationMetadata(properties);
    }

    public String get(String key) {
        return properties.get(key);
    }

    public boolean contains(String key) {
        return properties.containsKey(key);
    }

    public Map<String, String> getAll() {
        return properties;
    }

    public boolean isEmpty() {
        return properties.isEmpty();
    }

    public int size() {
        return properties.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationMetadata that = (NotificationMetadata) o;
        return Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(properties);
    }

    @Override
    public String toString() {
        return String.format("NotificationMetadata{properties=%d}", properties.size());
    }
}
