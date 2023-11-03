package com.generoso.salescatalog.converter

import com.generoso.salescatalog.dto.ProductV1Dto
import com.generoso.salescatalog.entity.Product
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.*

class ProductV1ConverterTest {

    private val converter = ProductV1Converter()

    @Test
    fun shouldConvertDtoToEntityAsExpected() {
        // Arrange
        val dto = ProductV1Dto()
        dto.name = "Test Product"
        dto.description = "Test Description"
        dto.price = BigDecimal.valueOf(9.99)
        dto.quantity = 50
        dto.reserved = true
        dto.sold = true

        // Act
        val entity = converter.convertToEntity(dto)

        // Assert
        assertEquals(dto.name, entity.name)
        assertEquals(dto.description, entity.description)
        assertEquals(dto.price, entity.price)
        assertEquals(dto.quantity, entity.quantity)
        assertNull(entity.productId)
        assertFalse(entity.isReserved)
        assertFalse(entity.isSold)
        assertNull(entity.salesUserId)
    }

    @Test
    fun shouldConvertFieldsToDtoAsExpected() {
        // Arrange
        val entity = Product(
            productId = UUID.randomUUID(),
            name = "Test Product",
            description = "Test Description",
            price = BigDecimal.valueOf(9.99),
            quantity = 50,
            isReserved = true,
            isSold = true
        )

        // Act
        val dto = converter.convertToDto(entity)

        // Assert
        assertEquals(entity.getId(), dto.id)
        assertEquals(entity.name, dto.name)
        assertEquals(entity.description, dto.description)
        assertEquals(entity.price, dto.price)
        assertEquals(entity.quantity, dto.quantity)
        assertEquals(entity.isReserved, dto.reserved)
        assertEquals(entity.isSold, dto.sold)
    }

    @Test
    fun testConvertToClientViewDto() {
        // Arrange
        val entity = Product(
            productId = UUID.randomUUID(),
            name = "Test Product",
            description = "Test Description",
            price = BigDecimal.valueOf(9.99),
            quantity = 50,
            isReserved = false,
            isSold = false
        )

        // Act
        val dto = converter.convertToPublicViewDto(entity)

        // Assert
        assertEquals(entity.getId(), dto.id)
        assertEquals(entity.name, dto.name)
        assertEquals(entity.description, dto.description)
        assertEquals(entity.price, dto.price)
        assertEquals(entity.quantity, dto.quantity)
        // Ensure isReserved and isSold are not set in the client view DTO
        assertNull(dto.reserved)
        assertNull(dto.sold)
    }
}