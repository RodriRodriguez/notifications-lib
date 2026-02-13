package com.notifications.model;

import java.util.Objects;

public final class Recipient {
    
    private final String address;
    private final String name;

    public Recipient(String address, String name) {
        this.address = Objects.requireNonNull(address, "Address cannot be null");
        this.name = name;
    }

    public Recipient(String address) {
        this(address, null);
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public boolean hasName() {
        return name != null && !name.trim().isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipient recipient = (Recipient) o;
        return Objects.equals(address, recipient.address) &&
                Objects.equals(name, recipient.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, name);
    }

    @Override
    public String toString() {
        if (hasName()) {
            return String.format("Recipient{address='%s', name='%s'}", address, name);
        }
        return String.format("Recipient{address='%s'}", address);
    }
}
