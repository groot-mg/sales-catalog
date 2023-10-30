package com.generoso.salescatalog.auth

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.util.*

class UserInfo {

    fun getUserId(): UUID {
        val contextHolder = contextHolder()
        return UUID.fromString((contextHolder.principal as Jwt).claims["sub"] as String)
    }

    fun getUsername(): String {
        val contextHolder = contextHolder()
        return (contextHolder.principal as Jwt).claims["username"] as String;
    }

    fun getRole(): UserRole {
        val contextHolder = contextHolder()
        val roles = getRoles(contextHolder.principal as Jwt);

        return when {
            roles.contains(UserRole.CLIENT.role) -> UserRole.CLIENT
            roles.contains(UserRole.SALES.role) -> UserRole.SALES
            else -> UserRole.UNKNOWN
        }
    }

    private fun contextHolder(): JwtAuthenticationToken {
        return SecurityContextHolder.getContext().authentication as JwtAuthenticationToken
    }

    private fun getRoles(jwt: Jwt): Collection<String> {
        val realmAccess = jwt.getClaim<Map<String, List<String>>>("realm_access")
        return realmAccess?.get("roles") ?: emptyList()
    }
}