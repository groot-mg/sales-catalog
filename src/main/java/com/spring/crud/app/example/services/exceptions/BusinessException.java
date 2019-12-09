package com.spring.crud.app.example.services.exceptions;

/**
 * Exception to business rules
 *
 * @author Mauricio Generoso
 */
public class BusinessException extends RuntimeException {

    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
    }
}
