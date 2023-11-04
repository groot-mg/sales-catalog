package com.generoso.salescatalog.service.validator

import com.generoso.salescatalog.auth.UserInfo
import com.generoso.salescatalog.entity.Product
import com.generoso.salescatalog.exception.DuplicateException
import com.generoso.salescatalog.repository.ProductRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class DuplicatedItemValidatorTest {

    @Mock
    private lateinit var repository: ProductRepository

    @Mock
    private lateinit var userInfo: UserInfo

    @InjectMocks
    private lateinit var validator: DuplicatedItemValidator

    @Test
    fun `validate throws DuplicateException when item is duplicated`() {
        // Arrange
        val userId = UUID.randomUUID()
        val product = Product(name = "Test Product")
        val existingProduct = Optional.of(Product())

        `when`(userInfo.getUserId()).thenReturn(userId)
        `when`(repository.findByNameAndSalesUserId(product.name!!, userId)).thenReturn(existingProduct)

        // Act & Assert
        val exception = assertThrows(DuplicateException::class.java) {
            validator.validate(product)
        }

        assertThat(exception.message).isEqualTo("Duplicated product with the same name")
    }

    @Test
    fun `validate does not throw exception when item is not duplicated`() {
        // Arrange
        val userId = UUID.randomUUID()
        val product = Product(name = "Test Product")
        val existingProduct = Optional.empty<Product>()

        `when`(userInfo.getUserId()).thenReturn(userId)
        `when`(repository.findByNameAndSalesUserId(product.name!!, userId)).thenReturn(existingProduct)

        // Act & Assert
        assertDoesNotThrow {
            validator.validate(product)
        }
    }
}
