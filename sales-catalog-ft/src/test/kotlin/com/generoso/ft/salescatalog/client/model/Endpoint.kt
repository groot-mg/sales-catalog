package com.generoso.ft.salescatalog.client.model

enum class Endpoint(val path: String, val method: String) {
    PRIVATE_INFO("/sales-catalog/private/info", "GET"),
    PRIVATE_HEALTH_CHECK("/sales-catalog/private/health", "GET"),
    PRIVATE_METRICS("/sales-catalog/private/metrics", "GET"),
}