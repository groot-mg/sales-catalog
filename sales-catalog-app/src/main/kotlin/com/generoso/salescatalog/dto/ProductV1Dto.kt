package com.generoso.salescatalog.dto

import java.math.BigDecimal
import java.util.*

data class ProductV1Dto @JvmOverloads constructor(
    val id: UUID? = null,
    val name: String? = null,
    val description: String? = null,
    val price: BigDecimal? = null,
    val quantity: Long? = null,
    val reserved: Boolean? = null,
    val sold: Boolean? = null,
)