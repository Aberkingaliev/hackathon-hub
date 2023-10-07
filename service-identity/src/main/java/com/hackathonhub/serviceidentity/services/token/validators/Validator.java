package com.hackathonhub.serviceidentity.services.token.validators;

import com.hackathonhub.serviceidentity.services.token.TokenValidationContext;

/**
 * Central interface for token validators
 */
public interface Validator {
    /**
     * Validation logic method
     * @param context - @see {@link TokenValidationContext}
     */
    void doValidation(TokenValidationContext context);

    /**
     * Priority of the validator
     * Minus values are prioritized first
     * e.g. -1 will be prioritized over 0
     * @return - int
     */
    int getPriority();
}
