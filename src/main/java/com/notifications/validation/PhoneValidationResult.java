package com.notifications.validation;

import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Optional;

public final class PhoneValidationResult {
    
    private final boolean valid;
    private final Phonenumber.PhoneNumber parsedNumber;
    private final String errorMessage;

    private PhoneValidationResult(boolean valid, Phonenumber.PhoneNumber parsedNumber, String errorMessage) {
        this.valid = valid;
        this.parsedNumber = parsedNumber;
        this.errorMessage = errorMessage;
    }

    public static PhoneValidationResult valid(Phonenumber.PhoneNumber parsedNumber) {
        return new PhoneValidationResult(true, parsedNumber, null);
    }

    public static PhoneValidationResult invalid(String errorMessage) {
        return new PhoneValidationResult(false, null, errorMessage);
    }

    public boolean isValid() {
        return valid;
    }

    public Optional<Phonenumber.PhoneNumber> getParsedNumber() {
        return Optional.ofNullable(parsedNumber);
    }

    public Optional<String> getErrorMessage() {
        return Optional.ofNullable(errorMessage);
    }

    @Override
    public String toString() {
        if (valid) {
            return String.format("PhoneValidationResult{valid=true, number=%s}", 
                    parsedNumber != null ? parsedNumber.toString() : "null");
        } else {
            return String.format("PhoneValidationResult{valid=false, error='%s'}", errorMessage);
        }
    }
}
