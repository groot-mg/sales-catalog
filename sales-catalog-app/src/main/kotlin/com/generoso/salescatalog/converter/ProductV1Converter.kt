package com.generoso.salescatalog.converter

import com.generoso.salescatalog.dto.ProductV1Dto
import com.generoso.salescatalog.entity.Product
import org.springframework.stereotype.Component

@Component
class ProductV1Converter {

    fun convertToEntity(dto: ProductV1Dto): Product {
        val product = Product()
        product.name = dto.name
        product.description = dto.description
        product.price = dto.price
        product.quantity = dto.quantity!!
        return product
    }

    fun convertToDto(entity: Product): ProductV1Dto {
        return ProductV1Dto(
            id = entity.getId(),
            name = entity.name,
            description = entity.description,
            price = entity.price,
            quantity = entity.quantity,
            reserved = entity.isReserved,
            sold = entity.isSold
        )
    }

    fun convertToClientViewDto(entity: Product): ProductV1Dto {
        return ProductV1Dto(
            id = entity.getId(),
            name = entity.name,
            description = entity.description,
            price = entity.price,
            quantity = entity.quantity
        )
    }
}