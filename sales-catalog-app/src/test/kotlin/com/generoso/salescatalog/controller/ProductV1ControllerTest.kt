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
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.math.BigDecimal

@Import(ProductV1Controller::class)
@WebMvcTest(ProductV1Controller::class)
class ProductV1ControllerTest : SecurityControllerSetup() {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var service: ProductService

    @MockBean
    private lateinit var converter: ProductV1Converter

    // Register product - POST
    @Test
    fun whenClientUserCallsToRegisterNewProduct_shouldReturnForbidden() {
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
    fun whenCallToSaveTheNewProduct_shouldCallConverterAndService() {
        // Arrange
        val productDto: ProductV1Dto = ProductV1Dto()
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