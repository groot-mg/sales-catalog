package com.generoso.salescatalog.exception

import com.generoso.salescatalog.exception.error.ErrorDetail
import com.generoso.salescatalog.exception.error.ValidationErrorDetails
import com.generoso.salescatalog.exception.error.ValidationErrorFields
import org.postgresql.util.PSQLException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

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

        return ResponseEntity(errorDetails, statusBadRequest)
    }

    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResourceFoundException(exception: NoResourceFoundException): ResponseEntity<Any>? {
        val statusCode = HttpStatus.NOT_FOUND
        //@formatter:off
        val errorDetail: ErrorDetail = ErrorDetail.builder()
            .status(statusCode.value())
            .detail(exception.message)
            .dateTime(LocalDateTime.now())
            .build()
        //@formatter:on
        return ResponseEntity<Any>(errorDetail, statusCode)
    }

    @ExceptionHandler(DuplicateException::class)
    fun handleDuplicateException(exception: DuplicateException): ResponseEntity<Any>? {
        val statusCode = HttpStatus.CONFLICT
        //@formatter:off
        val errorDetail: ErrorDetail = ErrorDetail.builder()
            .status(statusCode.value())
            .detail(exception.message)
            .dateTime(LocalDateTime.now())
            .build()
        //@formatter:on
        return ResponseEntity<Any>(errorDetail, statusCode)
    }

    @ExceptionHandler(PSQLException::class)
    fun handlePSQLException(exception: PSQLException): ResponseEntity<Any>? {
        val statusCode = HttpStatus.INTERNAL_SERVER_ERROR
        //@formatter:off
        val errorDetail: ErrorDetail = ErrorDetail.builder()
            .status(statusCode.value())
            .detail("Database exception")
            .dateTime(LocalDateTime.now())
            .build()
        //@formatter:on
        return ResponseEntity<Any>(errorDetail, statusCode)
    }
}