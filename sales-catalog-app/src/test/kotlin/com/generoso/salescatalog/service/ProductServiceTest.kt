package com.generoso.salescatalog.service

import com.generoso.salescatalog.auth.UserInfo
import com.generoso.salescatalog.entity.Product
import com.generoso.salescatalog.repository.ProductRepository
import org.junit.jupiter.api.Assertions.assertEquals
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
    private lateinit var productRepository: ProductRepository

    @Mock
    private lateinit var userInfo: UserInfo

    @InjectMocks
    private lateinit var productService: ProductService

    @Test
    fun whenFindingAllProductsForSalesUser_shouldCallRepositoryWithUserIdAndReturnAll() {
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
        `when`(productRepository.findBySalesUserId(userId, pageable)).thenReturn(expectedPage)

        // Act
        val products = productService.findAll(pageable)

        // Assert
        assertEquals(2, products.size)
        assertEquals("Product 1", products.content[0].name)
        assertEquals("Product 2", products.content[1].name)
        verify(productRepository).findBySalesUserId(userId, pageable)
    }

    @Test
    fun whenFindingAllProductsForNonSalesUser_shouldCallRepositoryAndReturnAll() {
        // Arrange
        val pageable = PageRequest.of(1, 20)

        val product1 = Product()
        product1.name = "Product 1"
        val product2 = Product()
        product2.name = "Product 2"
        val expectedPage = PageImpl(listOf(product1, product2))

        `when`(userInfo.isSalesUser()).thenReturn(false)
        `when`(productRepository.findAll(pageable)).thenReturn(expectedPage)

        // Act
        val products = productService.findAll(pageable)

        // Assert
        assertEquals(2, products.size)
        assertEquals("Product 1", products.content[0].name)
        assertEquals("Product 2", products.content[1].name)
        verify(productRepository).findAll(pageable)
    }

    @Test
    fun whenSavingTheProduct_shouldPassAllValidationsAndSaveIt() {
        // Arrange
        val productToSave = Product()
        productToSave.name = "New Product"

        `when`(productRepository.save(productToSave)).thenReturn(productToSave)

        // Act
        val savedProduct = productService.save(productToSave)

        // Assert
        assertEquals("New Product", savedProduct.name)
        verify(productRepository).save(productToSave)
    }
}