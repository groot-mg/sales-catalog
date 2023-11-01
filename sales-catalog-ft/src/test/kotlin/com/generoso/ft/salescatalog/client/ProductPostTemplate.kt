package com.generoso.ft.salescatalog.client

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.generoso.ft.salescatalog.client.model.Endpoint
import com.generoso.salescatalog.dto.ProductV1Dto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
@Qualifier("service-request")
class ProductPostTemplate @Autowired constructor(
    @Value("\${service.host}") host: String?,
    @Value("\${service.context-path:}") contextPath: String?
) : RequestTemplate(host!!, contextPath!!) {

    override val endpoint: Endpoint
        get() = Endpoint.PRODUCT_POST

    override fun getBody(): String? {
        val dto = ProductV1Dto()
        dto.name = "Product name"
        dto.description = "Product description"
        dto.price = BigDecimal.valueOf(10)
        dto.quantity = 50
        return jacksonObjectMapper().writeValueAsString(dto)
    }
}