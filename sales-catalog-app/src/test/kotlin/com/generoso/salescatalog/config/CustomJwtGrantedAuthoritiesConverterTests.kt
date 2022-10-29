package com.generoso.salescatalog.config

import com.nimbusds.jose.util.JSONObjectUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.security.oauth2.jwt.Jwt
import java.time.Instant

class CustomJwtGrantedAuthoritiesConverterTests {

    private val converter = CustomJwtGrantedAuthoritiesConverter()

    @Test
    fun whenRealmAccessDoesNotExist_returnsEmpty() {
        // Arrange && Act
        val grantedAuthority = converter.convert(createJwt(hashMapOf("any" to "any")))

        // Assert
        assertThat(grantedAuthority).isEmpty()
    }

    @Test
    fun whenRoleDoesNotExist_returnsEmpty() {
        // Arrange
        val roles = JSONObjectUtils.parse(
            """
            {"roles": null}
        """
        )
        val jwt = createJwt(hashMapOf("realm_access" to roles))

        // Act
        val grantedAuthority = converter.convert(jwt)

        // Assert
        assertThat(grantedAuthority).isEmpty()
    }

    @Test
    fun whenRolesIsEmpty_returnsEmpty() {
        // Arrange
        val roles = createRoles(ArrayList())
        val jwt = createJwt(hashMapOf("realm_access" to roles))

        // Act
        val grantedAuthority = converter.convert(jwt)

        // Assert
        assertThat(grantedAuthority).isEmpty()
    }

    @Test
    fun whenAListOfRolesExist_shouldReturnsTheMappedAuthorities() {
        // Arrange
        val roles = createRoles(listOf("api-client", "api-sales"))
        val jwt = createJwt(hashMapOf("realm_access" to roles))

        // Act
        val grantedAuthority = converter.convert(jwt)

        // Assert
        assertThat(grantedAuthority.size).isEqualTo(2)
        assertThat(grantedAuthority).anyMatch { item -> item.authority.equals("ROLE_api-client") }
        assertThat(grantedAuthority).anyMatch { item -> item.authority.equals("ROLE_api-sales") }
    }

    private fun createRoles(roles: List<String>): Map<String, Any> {
        val stringBuilder = StringBuilder()
        stringBuilder.append("{\"roles\": [")

        if (roles.isNotEmpty()) {
            roles.forEach { role -> stringBuilder.append("\"").append(role).append("\",") }
            stringBuilder.deleteCharAt(stringBuilder.length - 1)
        }

        stringBuilder.append("]}")
        return JSONObjectUtils.parse(stringBuilder.toString())
    }

    private fun createJwt(claims: Map<String, Any>): Jwt {
        return Jwt(
            "token",
            Instant.now(),
            Instant.now(),
            hashMapOf("any" to "any") as Map<String, Any>?,
            claims
        )
    }
}