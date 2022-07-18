package com.generoso.shopping.lib.exception;

import lombok.NoArgsConstructor;

/**
 * Exception thrown when a resource is not found
 *
 * @author Mauricio Generoso
 */
@NoArgsConstructor
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
