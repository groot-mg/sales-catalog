package com.spring.crud.app.example.services.exceptions;

/**
 * Exception to throws when not found a resource
 *
 * @author Mauricio Generoso
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
