package com.generoso.ft.salescatalog.steps

import com.generoso.ft.salescatalog.state.ScenarioState
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import io.cucumber.java.Before
import io.cucumber.java.en.And
import org.jose4j.jwk.JsonWebKeySet
import org.jose4j.jwk.RsaJsonWebKey
import org.jose4j.jwk.RsaJwkGenerator
import org.jose4j.jws.AlgorithmIdentifiers
import org.jose4j.jws.JsonWebSignature
import org.jose4j.jwt.JwtClaims
import org.jose4j.jwt.NumericDate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import java.lang.String.format
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

class SecurityStepsDefinitions @Autowired constructor(
    private val wiremock: WireMockServer,
    private val scenarioState: ScenarioState
) {

    companion object {
        const val KEYCLOAK_REALM = "groot-mg"
        private var rsaJsonWebKey: RsaJsonWebKey = RsaJwkGenerator.generateJwk(2048)

        init {
            rsaJsonWebKey.keyId = "k1"
            rsaJsonWebKey.algorithm = AlgorithmIdentifiers.RSA_USING_SHA256
            rsaJsonWebKey.use = "sig"
        }
    }

    @Value(value = "\${wiremock.host}")
    private val wiremockHost: String? = null

    @Value(value = "\${wiremock.port}")
    private val wiremockPort = 0

    @Before
    fun beforeEach() {
        mockKeycloakCertificate()
    }

    fun generateJWT(roles: List<String>): String {
        return generateJWT("default name", roles)
    }

    @And("use a JWT token for user {word} with role {word}")
    fun useJwtTokenForUserWithRole(user: String, role: String) {
        scenarioState.requestTemplate?.withHeader("Authorization", format("Bearer %s", generateJWT(user, listOf(role))))
    }

    @And("use a JWT token for user id {word} with role {word}")
    fun useJwtTokenForUserIdWithRole(userId: String, role: String) {
        scenarioState.requestTemplate?.withHeader("Authorization", format("Bearer %s", generateJWT(userId, "test-username", listOf(role))))
    }

    private fun generateJWT(name: String, roles: List<String>): String = generateJWT(UUID.randomUUID().toString(), name, roles)


    private fun generateJWT(userId: String, name: String, roles: List<String>): String {
        val claims = JwtClaims()
        claims.jwtId = UUID.randomUUID().toString()
        claims.setExpirationTimeMinutesInTheFuture(10f)
        claims.setNotBeforeMinutesInThePast(0f)
        claims.setIssuedAtToNow()
        claims.setAudience("account")
        claims.issuer = format("http://%s:%s/realms/%s", wiremockHost, wiremockPort, KEYCLOAK_REALM)
        claims.subject = UUID.fromString(userId).toString()
        claims.setClaim("typ", "Bearer")
        claims.setClaim("azp", "example-client-id")
        claims.setClaim(
            "auth_time",
            NumericDate.fromMilliseconds(Instant.now().minus(11, ChronoUnit.SECONDS).toEpochMilli()).value
        )
        claims.setClaim("session_state", UUID.randomUUID().toString())
        claims.setClaim("acr", "0")
        claims.setClaim("realm_access", mapOf("roles" to roles))
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

    private fun mockKeycloakCertificate() {
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
        wiremock.stubFor(get(urlEqualTo(format("/realms/%s/.well-known/openid-configuration", KEYCLOAK_REALM)))
            .willReturn(aResponse().withHeader("Content-Type", "application/json")
                .withBody(openidConfig)))
        //@formatter:on

        //@formatter:off
        wiremock.stubFor(get(urlEqualTo(format("/realms/%s/protocol/openid-connect/certs", KEYCLOAK_REALM)))
            .willReturn(aResponse().withHeader("Content-Type", "application/json")
                .withBody(JsonWebKeySet(rsaJsonWebKey).toJson())))
        //@formatter:on
    }
}