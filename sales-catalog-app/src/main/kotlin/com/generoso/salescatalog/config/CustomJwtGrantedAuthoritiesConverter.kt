package com.generoso.salescatalog.config

import com.nimbusds.jose.shaded.json.JSONArray
import com.nimbusds.jose.shaded.json.JSONObject
import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import java.util.*

/**
 * A custom implementation of [JwtGrantedAuthoritiesConverter] to get roles from "realm_access -> roles"
 */
class CustomJwtGrantedAuthoritiesConverter : Converter<Jwt?, Collection<GrantedAuthority>> {

    companion object {
        const val AUTHORITY_PREFIX = "ROLE_"
    }

    override fun convert(jwt: Jwt): Collection<GrantedAuthority> {
        val authorities = ArrayList<GrantedAuthority>()
        for (authority in getAuthorities(jwt)) {
            authorities.add(SimpleGrantedAuthority(AUTHORITY_PREFIX + authority))
        }
        return authorities
    }

    private fun getAuthorities(jwt: Jwt): Collection<String> {
        val realmAccess = jwt.getClaim<JSONObject>("realm_access")
        if (realmAccess?.get("roles") == null) {
            return Collections.emptyList()
        }

        val roles = realmAccess["roles"] as JSONArray
        val grantedRoles = ArrayList<String>()
        for (role in roles) {
            grantedRoles.add(role as String)
        }

        return grantedRoles
    }
}

