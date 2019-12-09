package com.spring.crud.app.example.services.exceptions;

/**
 * Exception to duplicate entity
 *
 * @author Mauricio Generoso
 */
public class DuplicateException extends RuntimeException {

    public DuplicateException() {
    }

    public DuplicateException(String message) {
        super(message);
    }
}
