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
        assertNull(errorDetail.validations)
    }

    @Test
    fun whenUsingBuilder_shouldFillAllCorrectValues() {
        // Arrange
        val status = 422
        val detail = "Validation Error"
        val dateTime = LocalDateTime.now()
        val errorDetails = arrayOf(ValidationErrorFields("name", arrayOf("must not be null")))

        // Act
        val validationErrorDetails = ValidationErrorDetails.builder()
            .status(status)
            .detail(detail)
            .dateTime(dateTime)
            .validations(errorDetails)
            .build()

        // Assert
        assertEquals(status, validationErrorDetails.status)
        assertEquals(detail, validationErrorDetails.detail)
        assertEquals(dateTime, validationErrorDetails.dateTime)
        assertEquals("name", errorDetails[0].field)
        assertEquals("must not be null", errorDetails[0].messages[0])
    }
}
