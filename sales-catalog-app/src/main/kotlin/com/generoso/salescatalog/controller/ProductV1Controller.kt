package com.generoso.salescatalog.controller

import com.generoso.salescatalog.converter.ProductV1Converter
import com.generoso.salescatalog.dto.ProductV1Dto
import com.generoso.salescatalog.entity.Product
import com.generoso.salescatalog.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products")
class ProductV1Controller @Autowired constructor(
    private val service: ProductService,
    private val converter: ProductV1Converter
) {

    @GetMapping
    fun getAll(): List<Product> = service.findAll()

    @PostMapping
    fun createProduct(@RequestBody dto: ProductV1Dto): ResponseEntity<ProductV1Dto> {
        val entity = converter.convertToEntity(dto)
        val savedEntity = service.save(entity)
        return ResponseEntity.status(HttpStatus.CREATED)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
            .body(converter.convertToDto(savedEntity))
    }
}