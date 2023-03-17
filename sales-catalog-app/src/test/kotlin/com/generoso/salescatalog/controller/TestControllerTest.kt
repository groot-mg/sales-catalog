package com.generoso.salescatalog.controller


import com.generoso.salescatalog.config.SecurityConfig
import com.github.tomakehurst.wiremock.client.WireMock.*
import org.jose4j.jwk.JsonWebKeySet
import org.jose4j.jwk.RsaJsonWebKey
import org.jose4j.jwk.RsaJwkGenerator
import org.jose4j.jws.AlgorithmIdentifiers
import org.jose4j.jws.JsonWebSignature
import org.jose4j.jwt.JwtClaims
import org.jose4j.jwt.NumericDate
import org.jose4j.lang.JoseException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.io.IOException
import java.lang.String.format
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [SecurityConfig::class])
@AutoConfigureWireMock(port = 8180)
@WebMvcTest(TestController::class)
@Import(TestController::class)
@TestPropertySource(locations = ["classpath:wiremock.properties"])
class TestControllerTest {

    private lateinit var rsaJsonWebKey: RsaJsonWebKey

    private var testSetupIsCompleted = false

    @Value("\${wiremock.server.baseUrl}")
    private val keycloakBaseUrl: String? = null

    @Value("\${keycloak.realm}")
    private val keycloakRealm: String? = null

    @Autowired
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    @Throws(IOException::class, JoseException::class)
    fun setUp() {
        if (!testSetupIsCompleted) {
            // Generate an RSA key pair, which will be used for signing and verification of the JWT, wrapped in a JWK
            rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048)
            rsaJsonWebKey.keyId = "k1"
            rsaJsonWebKey.algorithm = AlgorithmIdentifiers.RSA_USING_SHA256
            rsaJsonWebKey.use = "sig"
            val openidConfig = """{
              "issuer": "$keycloakBaseUrl/realms/$keycloakRealm",
              "authorization_endpoint": "$keycloakBaseUrl/realms/$keycloakRealm/protocol/openid-connect/auth",
              "token_endpoint": "$keycloakBaseUrl/realms/$keycloakRealm/protocol/openid-connect/token",
              "token_introspection_endpoint": "$keycloakBaseUrl/realms/$keycloakRealm/protocol/openid-connect/token/introspect",
              "userinfo_endpoint": "$keycloakBaseUrl/realms/$keycloakRealm/protocol/openid-connect/userinfo",
              "end_session_endpoint": "$keycloakBaseUrl/realms/$keycloakRealm/protocol/openid-connect/logout",
              "jwks_uri": "$keycloakBaseUrl/realms/$keycloakRealm/protocol/openid-connect/certs",
              "check_session_iframe": "$keycloakBaseUrl/realms/$keycloakRealm/protocol/openid-connect/login-status-iframe.html",
              "registration_endpoint": "$keycloakBaseUrl/realms/$keycloakRealm/clients-registrations/openid-connect",
              "introspection_endpoint": "$keycloakBaseUrl/realms/$keycloakRealm/protocol/openid-connect/token/introspect"
            }"""

            stubFor(
                get(urlEqualTo(format("/realms/%s/.well-known/openid-configuration", keycloakRealm)))
                    .willReturn(
                        aResponse()
                            .withHeader("Content-Type", "application/json")
                            .withBody(openidConfig)
                    )
            )

            stubFor(
                get(urlEqualTo(format("/realms/%s/protocol/openid-connect/certs", keycloakRealm)))
                    .willReturn(
                        aResponse()
                            .withHeader("Content-Type", "application/json")
                            .withBody(JsonWebKeySet(rsaJsonWebKey).toJson())
                    )
            )

            testSetupIsCompleted = true
        }
    }

    @Test
    fun when_access_token_is_in_header_Then_process_request_with_Ok() {
        val resultActions = mockMvc.perform(
            get("/test")
                .header("Authorization", format("Bearer %s", generateJWT()))
        )

        resultActions
//            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(content().string("hello"))
    }

    @Test
    @Throws(Exception::class)
    fun when_access_token_is_missing_Then_redirect_to_login() {
        val resultActions = mockMvc.perform(get("/test"))
        resultActions.andExpect(status().isUnauthorized)
    }

    @Throws(JoseException::class)
    private fun generateJWT(): String? {

        // Create the Claims, which will be the content of the JWT
        val claims = JwtClaims()
        claims.jwtId = UUID.randomUUID().toString() // a unique identifier for the token
        claims.setExpirationTimeMinutesInTheFuture(10f) // time when the token will expire (10 minutes from now)
        claims.setNotBeforeMinutesInThePast(0f) // time before which the token is not yet valid (2 minutes ago)
        claims.setIssuedAtToNow() // when the token was issued/created (now)
        claims.setAudience("account") // to whom this token is intended to be sent
        claims.issuer = format("%s/realms/%s", keycloakBaseUrl, keycloakRealm) // who creates the token and signs it
        claims.subject = UUID.randomUUID().toString() // the subject/principal is whom the token is about
        claims.setClaim("typ", "Bearer") // set type of token
        claims.setClaim("azp", "example-client-id") // Authorized party  (the party to which this token was issued)
        claims.setClaim(
            "auth_time",
            NumericDate.fromMilliseconds(Instant.now().minus(11, ChronoUnit.SECONDS).toEpochMilli()).value
        ) // time when authentication occured
        claims.setClaim("session_state", UUID.randomUUID().toString()) // keycloak specific ???
        claims.setClaim("acr", "0") //Authentication context class
        claims.setClaim(
            "realm_access",
            mapOf("roles" to listOf("api-client", "offline_access", "uma_authorization"))
        ) //keycloak roles
        claims.setClaim(
            "resource_access", mapOf(
                "account" to mapOf("roles" to listOf("manage-account", "manage-account-links", "view-profile"))
            )
        ) //keycloak roles
        claims.setClaim("scope", "profile email")
        claims.setClaim("name", "John Doe") // additional claims/attributes about the subject can be added
        claims.setClaim("email_verified", true)
        claims.setClaim("preferred_username", "doe.john")
        claims.setClaim("given_name", "John")
        claims.setClaim("family_name", "Doe")

        // A JWT is a JWS and/or a JWE with JSON claims as the payload.
        // In this example it is a JWS, so we create a JsonWebSignature object.
        val jws = JsonWebSignature()

        // The payload of the JWS is JSON content of the JWT Claims
        jws.payload = claims.toJson()

        // The JWT is signed using the private key
        jws.key = rsaJsonWebKey.privateKey

        // Set the Key ID (kid) header because it's just the polite thing to do.
        // We only have one key in this example but a using a Key ID helps
        // facilitate a smooth key rollover process
        jws.keyIdHeaderValue = rsaJsonWebKey.keyId

        // Set the signature algorithm on the JWT/JWS that will integrity protect the claims
        jws.algorithmHeaderValue = AlgorithmIdentifiers.RSA_USING_SHA256

        // set the type header
        jws.setHeader("typ", "JWT")

        // Sign the JWS and produce the compact serialization or the complete JWT/JWS
        // representation, which is a string consisting of three dot ('.') separated
        // base64url-encoded parts in the form Header.Payload.Signature
        return jws.compactSerialization
    }
}