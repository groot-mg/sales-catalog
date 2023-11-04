package com.generoso.salescatalog.service

import com.generoso.salescatalog.auth.UserInfo
import com.generoso.salescatalog.entity.Product
import com.generoso.salescatalog.exception.NoResourceFoundException
import com.generoso.salescatalog.repository.ProductRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*

@ExtendWith(MockitoExtension::class)
class ProductServiceTest {

    @Mock
    private lateinit var repository: ProductRepository

    @Mock
    private lateinit var userInfo: UserInfo

    @InjectMocks
    private lateinit var productService: ProductService

    @Test
    fun `findAll for sales user should call repository with user ID and return all products`() {
        // Arrange
        val userId = UUID.randomUUID()
        val pageable = PageRequest.of(1, 20)

        val product1 = Product()
        product1.name = "Product 1"
        val product2 = Product()
        product2.name = "Product 2"
        val expectedPage = PageImpl(listOf(product1, product2))

        `when`(userInfo.getUserId()).thenReturn(userId)
        `when`(userInfo.isSalesUser()).thenReturn(true)
        `when`(repository.findBySalesUserId(userId, pageable)).thenReturn(expectedPage)

        // Act
        val products = productService.findAll(pageable)

        // Assert
        assertEquals(2, products.size)
        assertEquals("Product 1", products.content[0].name)
        assertEquals("Product 2", products.content[1].name)
        verify(repository).findBySalesUserId(userId, pageable)
    }

    @Test
    fun `findAll for non-sales user should call repository and return all products`() {
        // Arrange
        val pageable = PageRequest.of(1, 20)

        val product1 = Product()
        product1.name = "Product 1"
        val product2 = Product()
        product2.name = "Product 2"
        val expectedPage = PageImpl(listOf(product1, product2))

        `when`(userInfo.isSalesUser()).thenReturn(false)
        `when`(repository.findAll(pageable)).thenReturn(expectedPage)

        // Act
        val products = productService.findAll(pageable)

        // Assert
        assertEquals(2, products.size)
        assertEquals("Product 1", products.content[0].name)
        assertEquals("Product 2", products.content[1].name)
        verify(repository).findAll(pageable)
    }

    @Test
    fun `findById should return product for sales user based on user ID`() {
        // Arrange
        val productId = UUID.randomUUID()
        val salesUserId = UUID.randomUUID()

        `when`(userInfo.isSalesUser()).thenReturn(true)
        `when`(userInfo.getUserId()).thenReturn(salesUserId)

        val expectedProduct = Product(productId, "Test Product")

        `when`(repository.findByIdAndSalesUserId(productId, salesUserId)).thenReturn(Optional.of(expectedProduct))

        // Act
        val result = productService.findById(productId)

        // Assert
        assertEquals(expectedProduct, result)
    }

    @Test
    fun `findById should return product for non-sales user`() {
        // Arrange
        val productId = UUID.randomUUID()
        `when`(userInfo.isSalesUser()).thenReturn(false)

        val expectedProduct = Product(productId, "Test Product")

        `when`(repository.findById(productId)).thenReturn(Optional.of(expectedProduct))

        // Act
        val result = productService.findById(productId)

        // Assert
        assertEquals(expectedProduct, result)
    }

    @Test
    fun `findById throws exception when product is not found`() {
        // Arrange
        val productId = UUID.randomUUID()
        `when`(userInfo.isSalesUser()).thenReturn(false)

        `when`(repository.findById(productId)).thenReturn(Optional.empty())

        // Act and Assert
        assertThrows(NoResourceFoundException::class.java) { productService.findById(productId) }
    }

    @Test
    fun `save should pass all validations and save product`() {
        // Arrange
        val productToSave = Product()
        productToSave.name = "New Product"

        `when`(repository.save(productToSave)).thenReturn(productToSave)

        // Act
        val savedProduct = productService.save(productToSave)

        // Assert
        assertEquals("New Product", savedProduct.name)
        verify(repository).save(productToSave)
    }

    @Test
    fun `delete sets isDeleted to true`() {
        // Arrange
        val product = Product(productId = UUID.randomUUID(), name = "Test Product", isDeleted = false)

        // Act
        productService.delete(product)

        // Assert
        assertTrue(product.isDeleted!!)
        verify(repository).save(product)
    }
}