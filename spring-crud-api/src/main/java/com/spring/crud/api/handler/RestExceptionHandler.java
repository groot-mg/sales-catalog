package com.spring.crud.api.handler;

import com.spring.crud.api.error.ErrorDetail;
import com.spring.crud.api.error.ResourceNotFoundDetails;
import com.spring.crud.api.error.ValidationErrorDetails;
import com.spring.crud.lib.exception.BusinessException;
import com.spring.crud.lib.exception.DuplicateException;
import com.spring.crud.lib.exception.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RestExceptionHandler class
 *
 * @author Mauricio Generoso
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception rfnException, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        //@formatter:off
        ErrorDetail errorDetail = ErrorDetail.Builder
                .newBuilder()
                .timestamp(new Date().getTime())
                .status(status.value())
                .title("Internal Exception")
                .detail(rfnException.getMessage())
                .developerMessage(rfnException.getClass().getName())
                .build();
        //@formatter:on
        return new ResponseEntity<>(errorDetail, headers, status);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException manvException,
                                                               HttpHeaders headers, HttpStatus status, WebRequest request) {

        HttpStatus statusBadRequest = HttpStatus.BAD_REQUEST;
        List<FieldError> fieldErrors = manvException.getBindingResult().getFieldErrors();
        String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(","));
        String fieldMessages = fieldErrors.stream().map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(","));

        //@formatter:off
        ValidationErrorDetails rnfDetails = ValidationErrorDetails.Builder
                .newBuilder()
                .timestamp(new Date().getTime())
                .status(statusBadRequest.value())
                .title("Field validation error")
                .detail("Field validation error")
                .developerMessage(manvException.getClass().getName())
                .field(fields)
                .fieldMessage(fieldMessages)
                .build();
        //@formatter:on
        return new ResponseEntity<>(rnfDetails, statusBadRequest);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity handleResourceNotFoundException(ResourceNotFoundException rfnException) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        //@formatter:off
        ResourceNotFoundDetails rnfDetails = ResourceNotFoundDetails.Builder
                .newBuilder()
                .timestamp(new Date().getTime())
                .status(status.value())
                .title("Resource Not Found")
                .detail(rfnException.getMessage())
                .developerMessage(rfnException.getClass().getName())
                .build();
        //@formatter:on
        return new ResponseEntity<>(rnfDetails, status);
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity handleDuplicateException(DuplicateException dException) {
        HttpStatus status = HttpStatus.CONFLICT;
        //@formatter:off
        ErrorDetail errorDetail = ErrorDetail.Builder
                .newBuilder()
                .timestamp(new Date().getTime())
                .status(status.value())
                .title("Conflict")
                .detail(dException.getMessage())
                .developerMessage(dException.getClass().getName())
                .build();
        //@formatter:on
        return new ResponseEntity<>(errorDetail, status);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity handleBusinessException(BusinessException bException) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        //@formatter:off
        ErrorDetail errorDetail = ErrorDetail.Builder
                .newBuilder()
                .timestamp(new Date().getTime())
                .status(status.value())
                .title("Business exception")
                .detail(bException.getMessage())
                .developerMessage(bException.getClass().getName())
                .build();
        //@formatter:on
        return new ResponseEntity<>(errorDetail, status);
    }
}