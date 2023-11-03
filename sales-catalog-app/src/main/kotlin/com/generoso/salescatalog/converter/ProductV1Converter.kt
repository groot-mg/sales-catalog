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
        val dto = ProductV1Dto(id = entity.getId())
        dto.name = entity.name
        dto.description = entity.description
        dto.price = entity.price
        dto.quantity = entity.quantity
        dto.reserved = entity.isReserved
        dto.sold = entity.isSold
        return dto
    }

    fun convertToPublicViewDto(entity: Product): ProductV1Dto {
        val dto = ProductV1Dto(id = entity.getId())
        dto.name = entity.name
        dto.description = entity.description
        dto.price = entity.price
        dto.quantity = entity.quantity
        return dto
    }
}