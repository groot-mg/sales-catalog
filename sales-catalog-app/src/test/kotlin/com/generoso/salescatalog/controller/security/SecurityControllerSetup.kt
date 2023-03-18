package com.generoso.salescatalog.controller.security

import com.generoso.salescatalog.config.SecurityConfig
import com.github.tomakehurst.wiremock.client.WireMock.*
import org.jose4j.jwk.JsonWebKeySet
import org.jose4j.jwk.RsaJsonWebKey
import org.jose4j.jwk.RsaJwkGenerator
import org.jose4j.jws.AlgorithmIdentifiers
import org.jose4j.jws.JsonWebSignature
import org.jose4j.jwt.JwtClaims
import org.jose4j.jwt.NumericDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.core.env.Environment
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import java.lang.String.format
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [SecurityConfig::class])
@AutoConfigureWireMock(port = 0)
@ActiveProfiles("unit-tests")
open class SecurityControllerSetup {

    companion object {
        const val KEYCLOAK_REALM = "test-realm-name"
        private var rsaJsonWebKey: RsaJsonWebKey = RsaJwkGenerator.generateJwk(2048)

        init {
            rsaJsonWebKey.keyId = "k1"
            rsaJsonWebKey.algorithm = AlgorithmIdentifiers.RSA_USING_SHA256
            rsaJsonWebKey.use = "sig"
        }
    }

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var environment: Environment

    @BeforeEach
    fun beforeEach() {
        mockKeycloakEndpoints()
    }

    fun generateJWT(roles: List<String>): String {
        return generateJWT("default name", roles)
    }

    fun generateJWT(name: String, roles: List<String>): String {
        // Create the Claims, which will be the content of the JWT
        val claims = JwtClaims()
        claims.jwtId = UUID.randomUUID().toString() // a unique identifier for the token
        claims.setExpirationTimeMinutesInTheFuture(10f) // time when the token will expire (10 minutes from now)
        claims.setNotBeforeMinutesInThePast(0f) // time before which the token is not yet valid (2 minutes ago)
        claims.setIssuedAtToNow() // when the token was issued/created (now)
        claims.setAudience("account") // to whom this token is intended to be sent
        claims.issuer = format(
            "http://localhost:%s/realms/%s",
            environment.getProperty("wiremock.server.port"), KEYCLOAK_REALM
        ) // who creates the token and signs it
        claims.subject = UUID.randomUUID().toString() // the subject/principal is whom the token is about
        claims.setClaim("typ", "Bearer") // set type of token
        claims.setClaim("azp", "example-client-id") // Authorized party  (the party to which this token was issued)
        claims.setClaim(
            "auth_time",
            NumericDate.fromMilliseconds(Instant.now().minus(11, ChronoUnit.SECONDS).toEpochMilli()).value
        ) // time when authentication occured
        claims.setClaim("session_state", UUID.randomUUID().toString())
        claims.setClaim("acr", "0") //Authentication context class
        claims.setClaim("realm_access", mapOf("roles" to roles)) //keycloak roles
        claims.setClaim(
            "resource_access",
            mapOf("account" to mapOf("roles" to listOf("manage-account", "manage-account-links", "view-profile")))
        )
        claims.setClaim("scope", "profile email")
        claims.setClaim("email_verified", true)
        claims.setClaim("preferred_username", name)

        val jws = JsonWebSignature()
        jws.payload = claims.toJson()
        jws.key = rsaJsonWebKey.privateKey
        jws.keyIdHeaderValue = rsaJsonWebKey.keyId
        jws.algorithmHeaderValue = AlgorithmIdentifiers.RSA_USING_SHA256
        jws.setHeader("typ", "JWT")
        return jws.compactSerialization
    }

    private fun mockKeycloakEndpoints() {
        val wiremockPort = environment.getProperty("wiremock.server.port")
        val openidConfig = """{
              "issuer": "http://localhost:$wiremockPort/realms/$KEYCLOAK_REALM",
              "authorization_endpoint": "http://localhost:$wiremockPort/realms/$KEYCLOAK_REALM/protocol/openid-connect/auth",
              "token_endpoint": "http://localhost:$wiremockPort/realms/$KEYCLOAK_REALM/protocol/openid-connect/token",
              "token_introspection_endpoint": "http://localhost:$wiremockPort/realms/$KEYCLOAK_REALM/protocol/openid-connect/token/introspect",
              "userinfo_endpoint": "http://localhost:$wiremockPort/realms/$KEYCLOAK_REALM/protocol/openid-connect/userinfo",
              "end_session_endpoint": "http://localhost:$wiremockPort/realms/$KEYCLOAK_REALM/protocol/openid-connect/logout",
              "jwks_uri": "http://localhost:$wiremockPort/realms/$KEYCLOAK_REALM/protocol/openid-connect/certs",
              "check_session_iframe": "http://localhost:$wiremockPort/realms/$KEYCLOAK_REALM/protocol/openid-connect/login-status-iframe.html",
              "registration_endpoint": "http://localhost:$wiremockPort/realms/$KEYCLOAK_REALM/clients-registrations/openid-connect",
              "introspection_endpoint": "http://localhost:$wiremockPort/realms/$KEYCLOAK_REALM/protocol/openid-connect/token/introspect"
            }"""

        //@formatter:off
        stubFor(get(urlEqualTo(format("/realms/%s/.well-known/openid-configuration",KEYCLOAK_REALM)))
            .willReturn(aResponse().withHeader("Content-Type", "application/json")
                .withBody(openidConfig)))
        //@formatter:on

        //@formatter:off
        stubFor(get(urlEqualTo(format("/realms/%s/protocol/openid-connect/certs", KEYCLOAK_REALM)))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                    .withBody(JsonWebKeySet(rsaJsonWebKey).toJson())))
        //@formatter:on
    }
}