package com.alejandrovillar.eats_hub_catalog.domain.exception;

/**
 * Exception thrown when a domain or application business rule is violated.
 */
public class BusinessException extends RuntimeException {
    /**
     * Creates a new business exception with a human-readable message.
     *
     * @param message description of the violated business rule
     */
    public BusinessException(String message) {
        super(message);
    }
}
