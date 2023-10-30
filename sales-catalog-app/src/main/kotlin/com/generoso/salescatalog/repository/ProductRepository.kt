package com.generoso.salescatalog.repository

import com.generoso.salescatalog.entity.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ProductRepository : JpaRepository<Product, UUID>