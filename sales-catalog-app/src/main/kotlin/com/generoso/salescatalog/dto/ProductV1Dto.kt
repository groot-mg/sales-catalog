package com.generoso.salescatalog.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.util.*

@Schema(description = "Details about a product")
class ProductV1Dto @JvmOverloads constructor(
    id: UUID? = null,
    val name: String? = null,
    val description: String? = null,
    val price: BigDecimal? = null,
    val quantity: Long? = null,
    @Schema(description = "weather a product is reserved is a client basket")
    val reserved: Boolean? = null,
    @Schema(description = "weather a product is already sold at least once")
    val sold: Boolean? = null,
) : BasicV1Dto(id)