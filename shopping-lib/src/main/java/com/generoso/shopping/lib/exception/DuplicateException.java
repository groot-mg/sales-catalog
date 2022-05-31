package com.generoso.shopping.lib.exception;

import lombok.NoArgsConstructor;

/**
 * Exception for duplicated entity
 *
 * @author Mauricio Generoso
 */
@NoArgsConstructor
public class DuplicateException extends RuntimeException {

    public DuplicateException(String message) {
        super(message);
    }
}
