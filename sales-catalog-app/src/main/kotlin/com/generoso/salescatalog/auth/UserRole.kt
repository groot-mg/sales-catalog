package com.generoso.salescatalog.auth

enum class UserRole(val role: String) {
    SALES("api-sales"),
    CLIENT("api-client"),
    UNKNOWN("unknown");

    companion object {
        fun fromString(role: String) = entries.find { it.role == role } ?: UNKNOWN
    }
}