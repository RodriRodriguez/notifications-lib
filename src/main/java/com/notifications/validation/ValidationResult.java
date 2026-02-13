package com.notifications.validation;

import java.util.Optional;

public final class ValidationResult {
    
    private final boolean valid;
    private final String errorMessage;

    private ValidationResult(boolean valid, String errorMessage) {
        this.valid = valid;
        this.errorMessage = errorMessage;
    }

    public static ValidationResult success() {
        return new ValidationResult(true, null);
    }

    public static ValidationResult failure(String errorMessage) {
        return new ValidationResult(false, errorMessage);
    }

    public boolean isValid() {
        return valid;
    }

    public boolean isInvalid() {
        return !valid;
    }

    public Optional<String> getErrorMessage() {
        return Optional.ofNullable(errorMessage);
    }

    @Override
    public String toString() {
        if (valid) {
            return "ValidationResult{valid=true}";
        }
        return String.format("ValidationResult{valid=false, error='%s'}", errorMessage);
    }
}
