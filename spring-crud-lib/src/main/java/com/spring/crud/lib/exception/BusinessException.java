package com.spring.crud.lib.exception;

/**
 * Exception for business rules
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
