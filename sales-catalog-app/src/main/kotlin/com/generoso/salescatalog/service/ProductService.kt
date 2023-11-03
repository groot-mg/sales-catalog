package com.generoso.salescatalog.service

import com.generoso.salescatalog.auth.UserInfo
import com.generoso.salescatalog.entity.Product
import com.generoso.salescatalog.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

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

    fun save(entity: Product): Product {
        validate(entity)
        return repository.save(entity)
    }

    private fun validate(product: Product) {
        // TODO: issue ##135
    }
}