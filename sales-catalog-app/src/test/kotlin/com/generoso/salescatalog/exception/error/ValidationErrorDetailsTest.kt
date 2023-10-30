package com.generoso.salescatalog.exception.error

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ValidationErrorDetailsTest {

    @Test
    fun whenBuilderDoesNoHaveAnyValue_shouldInitialiseWithDefaults() {
        // Act
        val errorDetail = ValidationErrorDetails.Builder().build()

        // Assert
        assertNull(errorDetail.status)
        assertNull(errorDetail.detail)
        assertNull(errorDetail.dateTime)
        assertNull(errorDetail.field)
        assertNull(errorDetail.fieldMessage)
    }

    @Test
    fun whenUsingBuilder_shouldFillAllCorrectValues() {
        // Arrange
        val status = 422
        val detail = "Validation Error"
        val dateTime = LocalDateTime.now()
        val field = "email"
        val fieldMessage = "Invalid email format"

        // Act
        val validationErrorDetails = ValidationErrorDetails.builder()
            .status(status)
            .detail(detail)
            .dateTime(dateTime)
            .field(field)
            .fieldMessage(fieldMessage)
            .build()

        // Assert
        assertEquals(status, validationErrorDetails.status)
        assertEquals(detail, validationErrorDetails.detail)
        assertEquals(dateTime, validationErrorDetails.dateTime)
        assertEquals(field, validationErrorDetails.field)
        assertEquals(fieldMessage, validationErrorDetails.fieldMessage)
    }
}
