package com.generoso.salescatalog.exception.error

import com.generoso.salescatalog.exception.GlobalExceptionHandler
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
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
    fun handleExceptionInternal_shouldContainsTheExpectedFields() {
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
    fun handleMethodArgumentNotValid_shouldContainsTheExpectedFieldsForASingleField() {
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
        assertEquals("email", errorDetail.field)
        assertEquals("Invalid email format", errorDetail.fieldMessage)
    }

    @Test
    fun handleMethodArgumentNotValid_shouldContainsTheExpectedFieldsForMultiFields() {
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
        assertEquals("name, email", errorDetail.field)
        assertEquals("name is invalid, Invalid email format", errorDetail.fieldMessage)
    }

    private fun createBindingResult(fields: Array<String>, messages: Array<String>): BindingResult {
        val bindingResult = org.springframework.validation.BeanPropertyBindingResult(null, "")
        fields.zip(messages).forEach { (field, message) ->
            bindingResult.addError(FieldError("", field, message))
        }
        return bindingResult
    }
}
