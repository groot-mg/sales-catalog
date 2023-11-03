package com.generoso.ft.salescatalog.model

data class Pageable(
    val pageNumber: Int = 0,
    val pageSize: Int = 0,
    val sort: Sort = Sort(),
    val offset: Int = 0,
    val paged: Boolean = false,
    val unpaged: Boolean = false
)
