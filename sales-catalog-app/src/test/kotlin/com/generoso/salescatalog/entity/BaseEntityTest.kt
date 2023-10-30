package com.generoso.salescatalog.entity

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class BaseEntityTest {

    @Test
    fun `test isNew() method with new entity`() {
        assertTrue(Product().isNew())
    }

    @Test
    fun `test isNew() method with existing entity`() {
        assertFalse(Product(productId = UUID.randomUUID()).isNew())
    }

    @Test
    fun `test equals() method with equal entities`() {
        // Arrange
        val entity1 = Product()
        val entity2 = Product()

        // Act & Assert
        assertTrue(entity1 == entity2)
    }

    @Test
    fun `test equals() method with non equals entities`() {
        assertFalse(Product().equals(String()))
    }

    @Test
    fun `test equals() method with same entity`() {
        val entity = Product()
        assertTrue(entity.equals(entity))
    }

    @Test
    fun `test equals() method with different ids`() {
        // Assert
        val entity1 = Product(productId = UUID.randomUUID())
        val entity2 = Product(productId = UUID.randomUUID())

        // Act & Assert
        assertFalse(entity1 == entity2)
    }

    @Test
    fun `test hashCode() method`() {
        val entity = Product()
        assertEquals(entity.javaClass.hashCode(), entity.hashCode())
    }

    @Test
    fun `test toString() method`() {
        // Arrange
        val id = UUID.randomUUID()
        val entity = Product(productId = id)

        // Act & Assert
        assertEquals("Entity of type com.generoso.salescatalog.entity.Product with id: $id", entity.toString())
    }
}