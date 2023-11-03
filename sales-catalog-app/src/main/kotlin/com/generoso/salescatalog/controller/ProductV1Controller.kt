package com.generoso.salescatalog.controller

import com.generoso.salescatalog.auth.UserInfo
import com.generoso.salescatalog.converter.ProductV1Converter
import com.generoso.salescatalog.dto.ProductV1Dto
import com.generoso.salescatalog.service.ProductService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "ProductV1Controller", description = "Controller to manage Products")
@RestController
@RequestMapping("/v1/products")
class ProductV1Controller @Autowired constructor(
    private val service: ProductService,
    private val converter: ProductV1Converter,
    private val userInfo: UserInfo
) {

    @Operation(description = "Retrieve a list of products")
    @GetMapping
    @ApiResponses(
        ApiResponse(
            responseCode = "200", description = "Successfully retrieved the list of products",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = Page::class)
            )]
        )
    )
    fun getAll(pageable: Pageable): Page<ProductV1Dto> {
        val products = service.findAll(pageable)
        return products.map {
            if (userInfo.isSalesUser())
                converter.convertToDto(it)
            else
                converter.convertToPublicViewDto(it)
        }
    }

    @Operation(description = "Register a new product")
    @PostMapping
    @ApiResponses(
        ApiResponse(
            responseCode = "201", description = "Successfully saved new product",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ProductV1Dto::class)
            )]
        )
    )
    fun createProduct(@Parameter(description = "Product dto to save") @RequestBody @Valid dto: ProductV1Dto): ResponseEntity<ProductV1Dto> {
        val entity = converter.convertToEntity(dto)
        val savedEntity = service.save(entity)
        return ResponseEntity.status(HttpStatus.CREATED)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
            .body(converter.convertToDto(savedEntity))
    }
}