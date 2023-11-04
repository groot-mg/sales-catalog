package com.generoso.salescatalog.repository

import com.generoso.salescatalog.entity.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProductRepository : JpaRepository<Product, UUID> {

    fun findBySalesUserId(userId: UUID, pageable: Pageable): Page<Product>

    fun findByIdAndSalesUserId(productId: UUID, userId: UUID): Optional<Product>

    fun findByNameAndSalesUserId(name: String, userId: UUID): Optional<Product>
}
