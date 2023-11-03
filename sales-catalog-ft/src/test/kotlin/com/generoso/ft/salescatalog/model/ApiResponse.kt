package com.generoso.ft.salescatalog.model

import com.generoso.salescatalog.dto.ProductV1Dto

data class ApiResponse(
    val content: List<ProductV1Dto> = listOf(),
    val pageable: Pageable = Pageable(),
    val last: Boolean = false,
    val totalPages: Int = 0,
    val totalElements: Int = 0,
    val size: Int = 0,
    val number: Int = 0,
    val sort: Sort = Sort(),
    val first: Boolean = true,
    val numberOfElements: Int = 0,
    val empty: Boolean = true
)