package com.generoso.salescatalog.service

import com.generoso.salescatalog.entity.Product
import com.generoso.salescatalog.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProductService @Autowired constructor(private val repository: ProductRepository) {

    fun findAll(): List<Product> {
        return repository.findAll()
    }

    fun save(entity: Product): Product {
        validate(entity)
        return repository.save(entity)
    }

    private fun validate(product: Product) {
        // TODO: issue ##135
    }
}