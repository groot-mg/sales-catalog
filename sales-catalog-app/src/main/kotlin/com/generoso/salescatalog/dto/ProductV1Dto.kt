package com.generoso.salescatalog.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Digits
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.util.*

@Schema(description = "Details about a product")
class ProductV1Dto @JvmOverloads constructor(
    id: UUID? = null,
) : BasicV1Dto(id) {

    @Size(max = 100)
    @NotBlank
    var name: String? = null

    @Size(max = 256)
    @NotBlank
    var description: String? = null

    @Digits(integer = 10, fraction = 2)
    @Positive
    var price: BigDecimal? = null

    @Positive
    var quantity: Long? = null

    @Schema(description = "weather a product is reserved is a client basket")
    var reserved: Boolean? = null

    @Schema(description = "weather a product is already sold at least once")
    var sold: Boolean? = null
}