package com.generoso.salescatalog.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.generoso.salescatalog.controller.security.SecurityControllerSetup
import com.generoso.salescatalog.converter.ProductV1Converter
import com.generoso.salescatalog.dto.ProductV1Dto
import com.generoso.salescatalog.entity.Product
import com.generoso.salescatalog.service.ProductService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.anyOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageImpl
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.math.BigDecimal
import java.util.*

@Import(ProductV1Controller::class)
@WebMvcTest(ProductV1Controller::class)
class ProductV1ControllerTest : SecurityControllerSetup() {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var service: ProductService

    @MockBean
    private lateinit var converter: ProductV1Converter

    // Retrieve products - GET
    @Test
    fun `given no authenticated user when get products then should return list of products`() {
        // Arrange
        val productId = UUID.randomUUID()
        val productName = "name"
        val products = listOf(Product(productId = productId, name = productName))
        val expectedPage = PageImpl(products)

        `when`(service.findAll(anyOrNull())).thenReturn(expectedPage)
        `when`(userInfo.isSalesUser()).thenReturn(false)
        `when`(converter.convertToPublicViewDto(products[0])).thenReturn(ProductV1Dto(id = productId).apply { this.name = productName })

        // Act & Assert
        //@formatter:off
        mockMvc.perform(get("/v1/products"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].id").value(productId.toString()))
            .andExpect(jsonPath("$.content[0].name").value(productName))
        //@formatter:on

        verify(service).findAll(anyOrNull())
        verify(userInfo).isSalesUser()
        verify(converter).convertToPublicViewDto(products[0])
    }

    @Test
    fun `when authenticated sales user calls get products should return successfully`() {
        // Arrange
        val productId = UUID.randomUUID()
        val productName = "name"
        val products = listOf(Product(productId = productId, name = productName))
        val expectedPage = PageImpl(products)

        `when`(service.findAll(anyOrNull())).thenReturn(expectedPage)
        `when`(userInfo.isSalesUser()).thenReturn(true)
        `when`(converter.convertToDto(products[0])).thenReturn(ProductV1Dto(id = productId).apply { this.name = productName })

        // Act & Assert
        //@formatter:off
        mockMvc.perform(get("/v1/products"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].id").value(productId.toString()))
            .andExpect(jsonPath("$.content[0].name").value(productName))
        //@formatter:on

        verify(service).findAll(anyOrNull())
        verify(userInfo).isSalesUser()
        verify(converter).convertToDto(products[0])
    }

    @Test
    fun `get product by id for a non-sales user should return the product successfully using public view`() {
        // Arrange
        val productId = UUID.randomUUID()
        val productName = "Test Product"
        val product = Product(productId, productName)

        `when`(service.findById(productId)).thenReturn(product)
        `when`(userInfo.isSalesUser()).thenReturn(false)
        `when`(converter.convertToPublicViewDto(product)).thenReturn(ProductV1Dto(id = productId).apply { this.name = productName })

        // Act & Assert
        mockMvc.perform(get("/v1/products/{productId}", productId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(productId.toString()))
            .andExpect(jsonPath("$.name").value(productName))

        verify(service).findById(productId)
        verify(userInfo).isSalesUser()
        verify(converter).convertToPublicViewDto(product)
    }

    @Test
    fun `get product by id for a sales user should return the product successfully using public sales view`() {
        // Arrange
        val productId = UUID.randomUUID()
        val productName = "Test Product"
        val product = Product(productId, productName)

        `when`(service.findById(productId)).thenReturn(product)
        `when`(userInfo.isSalesUser()).thenReturn(true)
        `when`(converter.convertToDto(product)).thenReturn(ProductV1Dto(id = productId).apply { this.name = productName })

        // Act & Assert
        mockMvc.perform(get("/v1/products/{productId}", productId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(productId.toString()))
            .andExpect(jsonPath("$.name").value(productName))

        verify(service).findById(productId)
        verify(userInfo).isSalesUser()
        verify(converter).convertToDto(product)
    }

    // Register product - POST
    @Test
    fun `when client user calls to register new product, should return forbidden`() {
        // Arrange
        val productDto = mock(ProductV1Dto::class.java)

        // Act & Assert
        //@formatter:off
        mockMvc.perform(
            post("/v1/products")
                .header("Authorization", clientUserToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto))
        ).andExpect(status().isForbidden)
        //@formatter:on
    }

    @Test
    fun `when call to save the new product, should call converter and service`() {
        // Arrange
        val productDto = ProductV1Dto()
        productDto.name = "fake-name"
        productDto.description = "fake-description"
        productDto.price = BigDecimal.valueOf(9.99)
        productDto.quantity = 50
        val product = mock(Product::class.java)

        `when`(converter.convertToEntity(anyOrNull())).thenReturn(product)
        `when`(service.save(product)).thenReturn(product)
        `when`(converter.convertToDto(product)).thenReturn(productDto)

        // Act & Assert
        //@formatter:off
        mockMvc.perform(
            post("/v1/products")
                .header("Authorization", salesUserToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto))
        ).andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        //@formatter:on

        verify(converter).convertToEntity(anyOrNull())
        verify(service).save(product)
        verify(converter).convertToDto(product)
    }
}