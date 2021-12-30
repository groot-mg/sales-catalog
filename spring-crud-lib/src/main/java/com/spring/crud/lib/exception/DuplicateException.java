package com.spring.crud.lib.exception;

/**
 * Exception for duplicated entity
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
