package com.spring.crud.lib.exception;

/**
 * Exception to throws when a resource is not found
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
