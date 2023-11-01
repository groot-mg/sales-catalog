package com.generoso.salescatalog.exception

import com.generoso.salescatalog.exception.error.ErrorDetail
import com.generoso.salescatalog.exception.error.ValidationErrorDetails
import com.generoso.salescatalog.exception.error.ValidationErrorFields
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    private val log = LoggerFactory.getLogger(this.javaClass)

    public override fun handleExceptionInternal(
        exception: Exception,
        body: Any?,
        headers: HttpHeaders,
        statusCode: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        //@formatter:off
        val errorDetail: ErrorDetail = ErrorDetail.builder()
            .status(statusCode.value())
            .detail(exception.message)
            .dateTime(LocalDateTime.now())
            .build()
        //@formatter:on
        return ResponseEntity<Any>(errorDetail, headers, statusCode)
    }

    public override fun handleMethodArgumentNotValid(
        exception: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {

        val statusBadRequest = HttpStatus.BAD_REQUEST
        val fieldErrors = exception.bindingResult.fieldErrors
        val validations: Array<ValidationErrorFields> = fieldErrors.groupBy({ it.field }, { it.defaultMessage ?: "" })
            .map { (fieldName, errorMessages) ->
                ValidationErrorFields(fieldName, errorMessages.toTypedArray())
            }.toTypedArray()

        //@formatter:off
        val errorDetails: ValidationErrorDetails = ValidationErrorDetails.builder()
            .status(statusBadRequest.value())
            .detail("Fields validation error")
            .dateTime(LocalDateTime.now())
            .validations(validations)
            .build()
        //@formatter:on

        log.info("Request failed with validation exception: {}", errorDetails, exception)
        return ResponseEntity(errorDetails, statusBadRequest)
    }
}