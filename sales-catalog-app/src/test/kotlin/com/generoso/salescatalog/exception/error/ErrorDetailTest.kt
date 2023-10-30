package com.generoso.salescatalog.exception.error

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ErrorDetailTest {

    @Test
    fun whenBuilderDoesNoHaveAnyValue_shouldInitialiseWithDefaults() {
        // Act
        val errorDetail = ErrorDetail.builder().build()

        // Assert
        assertNull(errorDetail.status)
        assertNull(errorDetail.detail)
        assertNull(errorDetail.dateTime)
    }

    @Test
    fun whenUsingBuilder_shouldFillAllCorrectValues() {
        // Arrange
        val status = 404
        val detail = "Not Found"
        val dateTime = LocalDateTime.now()

        // Act
        val errorDetail = ErrorDetail.builder()
            .status(status)
            .detail(detail)
            .dateTime(dateTime)
            .build()

        // Assert
        assertEquals(status, errorDetail.status)
        assertEquals(detail, errorDetail.detail)
        assertEquals(dateTime, errorDetail.dateTime)
    }
}
