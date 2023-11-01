package com.generoso.salescatalog.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.*
import java.math.BigDecimal
import java.util.*

@Schema(description = "Details about a product")
class ProductV1Dto @JvmOverloads constructor(
    id: UUID? = null,
) : BasicV1Dto(id) {

    @Size(min = 5, max = 100)
    @NotBlank
    var name: String? = null

    @Size(max = 256)
    var description: String? = null

    @Digits(integer = 8, fraction = 2)
    @NotNull
    @Positive
    var price: BigDecimal? = null

    @NotNull
    @Positive
    var quantity: Long? = null

    @Schema(description = "weather a product is reserved is a client basket")
    var reserved: Boolean? = null

    @Schema(description = "weather a product is already sold at least once")
    var sold: Boolean? = null
}