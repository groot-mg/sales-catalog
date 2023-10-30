package com.generoso.salescatalog.service

import com.generoso.salescatalog.entity.Product
import com.generoso.salescatalog.repository.ProductRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class ProductServiceTest {

    @Mock
    private lateinit var productRepository: ProductRepository

    @InjectMocks
    private lateinit var productService: ProductService

    @Test
    fun whenFindingAllProducts_shouldCallRepositoryAndReturnAll() {
        // Arrange
        val product1 = Product()
        product1.name = "Product 1"

        val product2 = Product()
        product2.name = "Product 2"

        `when`(productRepository.findAll()).thenReturn(listOf(product1, product2))

        // Act
        val products = productService.findAll()

        // Assert
        assertEquals(2, products.size)
        assertEquals("Product 1", products[0].name)
        assertEquals("Product 2", products[1].name)
        verify(productRepository).findAll()
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