package com.generoso.salescatalog.exception

import com.generoso.salescatalog.exception.error.ErrorDetail
import com.generoso.salescatalog.exception.error.ValidationErrorDetails
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.postgresql.util.PSQLException
import org.postgresql.util.PSQLState
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.context.request.ServletWebRequest
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class GlobalExceptionHandlerTest {

    private val globalExceptionHandler = GlobalExceptionHandler()

    @Test
    fun `handleExceptionInternal should contain expected fields`() {
        // Arrange
        val exception = Exception("Exception message")
        val headers = HttpHeaders()
        val request = MockHttpServletRequest()
        val webRequest = ServletWebRequest(request)

        // Act
        val responseEntity: ResponseEntity<Any>? = globalExceptionHandler.handleExceptionInternal(
            exception,
            null,
            headers,
            HttpStatus.NOT_FOUND,
            webRequest
        )

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity?.statusCode)
        val errorDetail = responseEntity?.body as ErrorDetail
        assertEquals(404, errorDetail.status)
        assertEquals("Exception message", errorDetail.detail)
        val errorDateTime = errorDetail.dateTime!!
        assertTrue(
            errorDateTime.isAfter(LocalDateTime.now().minusMinutes(1)) &&
                    errorDateTime.isBefore(LocalDateTime.now().plusMinutes(1))
        )
    }

    @Test
    fun `handleMethodArgumentNotValid should contain expected fields for a single field`() {
        // Arrange
        val exception = MethodArgumentNotValidException(
            mock(MethodParameter::class.java),
            createBindingResult(arrayOf("email"), arrayOf("Invalid email format"))
        )
        val headers = HttpHeaders()
        val request = MockHttpServletRequest()
        val webRequest = ServletWebRequest(request)

        // Act
        val responseEntity: ResponseEntity<Any>? = globalExceptionHandler.handleMethodArgumentNotValid(
            exception,
            headers,
            HttpStatus.BAD_REQUEST,
            webRequest
        )

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity?.statusCode)
        val errorDetail = responseEntity?.body as ValidationErrorDetails
        assertEquals(400, errorDetail.status)
        assertEquals("Fields validation error", errorDetail.detail)
        assertEquals(LocalDateTime.now().dayOfYear, errorDetail.dateTime?.dayOfYear)
        assertEquals("email", errorDetail.validations?.get(0)?.field)
        assertEquals("Invalid email format", errorDetail.validations?.get(0)?.messages?.get(0))
    }

    @Test
    fun `handleMethodArgumentNotValid should contain expected fields for multiple fields`() {
        // Arrange
        val exception = MethodArgumentNotValidException(
            mock(MethodParameter::class.java),
            createBindingResult(arrayOf("name", "email"), arrayOf("name is invalid", "Invalid email format"))
        )
        val headers = HttpHeaders()
        val request = MockHttpServletRequest()
        val webRequest = ServletWebRequest(request)

        // Act
        val responseEntity: ResponseEntity<Any>? = globalExceptionHandler.handleMethodArgumentNotValid(
            exception,
            headers,
            HttpStatus.BAD_REQUEST,
            webRequest
        )

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity?.statusCode)
        val errorDetail = responseEntity?.body as ValidationErrorDetails
        assertEquals(400, errorDetail.status)
        assertEquals("Fields validation error", errorDetail.detail)
        assertEquals(LocalDateTime.now().dayOfYear, errorDetail.dateTime?.dayOfYear)
        assertEquals("name", errorDetail.validations?.get(0)?.field)
        assertEquals("name is invalid", errorDetail.validations?.get(0)?.messages?.get(0))
        assertEquals("email", errorDetail.validations?.get(1)?.field)
        assertEquals("Invalid email format", errorDetail.validations?.get(1)?.messages?.get(0))
    }

    @Test
    fun `handleNoResourceFoundException should return a ResponseEntity with correct status and message`() {
        // Arrange
        val exceptionMessage = "Resource not found"
        val exception = NoResourceFoundException(exceptionMessage)

        // Act
        val responseEntity: ResponseEntity<Any>? = globalExceptionHandler.handleNoResourceFoundException(exception)

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity?.statusCode)
        assertEquals(exceptionMessage, (responseEntity?.body as ErrorDetail).detail)
    }

    @Test
    fun `handleDuplicateException should return a ResponseEntity with correct status and message`() {
        // Arrange
        val exceptionMessage = "Duplicated item"
        val exception = DuplicateException(exceptionMessage)

        // Act
        val responseEntity: ResponseEntity<Any>? = globalExceptionHandler.handleDuplicateException(exception)

        // Assert
        assertEquals(HttpStatus.CONFLICT, responseEntity?.statusCode)
        assertEquals(exceptionMessage, (responseEntity?.body as ErrorDetail).detail)
    }

    @Test
    fun `handlePSQLException should return 500 and expected message`() {
        // Arrange
        val expectedStatusCode = HttpStatus.INTERNAL_SERVER_ERROR
        val expectedDetail = "Database exception"

        // Act
        val responseEntity = globalExceptionHandler.handlePSQLException(PSQLException(expectedDetail, PSQLState.UNKNOWN_STATE, null))

        // Assert
        assertEquals(expectedStatusCode, responseEntity?.statusCode)
        assertEquals(expectedDetail, (responseEntity?.body as ErrorDetail).detail)
    }

    private fun createBindingResult(fields: Array<String>, messages: Array<String>): BindingResult {
        val bindingResult = org.springframework.validation.BeanPropertyBindingResult(null, "")
        fields.zip(messages).forEach { (field, message) ->
            bindingResult.addError(FieldError("", field, message))
        }
        return bindingResult
    }
}
