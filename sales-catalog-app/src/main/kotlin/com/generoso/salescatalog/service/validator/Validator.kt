package com.generoso.salescatalog.service.validator

import com.generoso.salescatalog.entity.BaseEntity

abstract class Validator<E : BaseEntity<*>> {
    abstract fun validate(entity: E)
}

