package com.generoso.ft.salescatalog.client.model

enum class Endpoint(val path: String, val method: String) {
    PRIVATE_INFO("/sales-catalog/private/info", "GET"),
    PRIVATE_HEALTH_CHECK("/sales-catalog/private/health", "GET"),
    PRIVATE_METRICS("/sales-catalog/private/metrics", "GET"),
    PRIVATE_OPEN_API("/sales-catalog/private/api-docs", "GET"),
    PRIVATE_SWAGGER_REDIRECTION("/sales-catalog/private/swagger", "GET"),
    PRIVATE_SWAGGER_UI("/sales-catalog/private/swagger-ui/index.html", "GET"),
    PRODUCT_POST("/sales-catalog/v1/products", "POST"),
    PRODUCT_GET("/sales-catalog/v1/products", "GET")
}