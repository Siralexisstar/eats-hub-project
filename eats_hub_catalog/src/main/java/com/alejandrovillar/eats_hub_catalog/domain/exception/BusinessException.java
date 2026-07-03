package com.alejandrovillar.eats_hub_catalog.domain.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
