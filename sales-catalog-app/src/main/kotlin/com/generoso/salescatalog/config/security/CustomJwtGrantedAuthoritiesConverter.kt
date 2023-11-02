package com.generoso.salescatalog.config.security

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap
import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter

/**
 * A custom implementation of [JwtGrantedAuthoritiesConverter] to get roles from "realm_access -> roles"
 */
class CustomJwtGrantedAuthoritiesConverter : Converter<Jwt?, Collection<GrantedAuthority>> {

    companion object {
        const val AUTHORITY_PREFIX = "ROLE_"
    }

    override fun convert(jwt: Jwt): Collection<GrantedAuthority> {
        return getAuthorities(jwt).map { SimpleGrantedAuthority(AUTHORITY_PREFIX + it) }
    }

    private fun getAuthorities(jwt: Jwt): Collection<String> {
        val realmAccess = jwt.getClaim<LinkedTreeMap<String, List<String>>>("realm_access")
        return realmAccess?.get("roles") ?: emptyList()
    }
}
