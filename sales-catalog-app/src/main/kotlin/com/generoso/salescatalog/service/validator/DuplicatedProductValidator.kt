package com.generoso.salescatalog.service.validator

import com.generoso.salescatalog.auth.UserInfo
import com.generoso.salescatalog.entity.Product
import com.generoso.salescatalog.exception.DuplicateException
import com.generoso.salescatalog.repository.ProductRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import java.util.*

@Component
@Qualifier("product-validator")
class DuplicatedItemValidator(
    private val repository: ProductRepository,
    private val userInfo: UserInfo
) : Validator<Product>() {

    override fun validate(entity: Product) {
        val existingItem: Optional<Product> = repository.findByNameAndSalesUserId(entity.name!!, userInfo.getUserId())
        if (isDuplicated(entity, existingItem)) {
            throw DuplicateException("Duplicated product with the same name")
        }
    }

    private fun isDuplicated(entity: Product, existingItem: Optional<Product>): Boolean {
        return entity.isNew() && existingItem.isPresent
    }
}