package com.generoso.salescatalog.service

import com.generoso.salescatalog.auth.UserInfo
import com.generoso.salescatalog.entity.Product
import com.generoso.salescatalog.exception.NoResourceFoundException
import com.generoso.salescatalog.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProductService @Autowired constructor(
    private val repository: ProductRepository,
    private val userInfo: UserInfo
) {

    fun findAll(pageable: Pageable): Page<Product> {
        if (userInfo.isSalesUser()) {
            return repository.findBySalesUserId(userInfo.getUserId(), pageable)
        }

        return repository.findAll(pageable)
    }

    fun findById(productId: UUID): Product {
        val product = if (userInfo.isSalesUser())
            repository.findByIdAndSalesUserId(productId, userInfo.getUserId())
        else
            repository.findById(productId)

        return product.orElseThrow { NoResourceFoundException("Product not found with id $productId") }
    }

    fun save(entity: Product): Product {
        validate(entity)
        return repository.save(entity)
    }

    private fun validate(product: Product) {
        // TODO: issue ##135
    }
}