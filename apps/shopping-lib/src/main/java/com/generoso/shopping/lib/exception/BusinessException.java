package com.generoso.shopping.lib.exception;

import lombok.NoArgsConstructor;

/**
 * Exception for business rules
 *
 * @author Mauricio Generoso
 */
@NoArgsConstructor
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
