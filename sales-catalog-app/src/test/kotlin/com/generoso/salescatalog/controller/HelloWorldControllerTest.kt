package com.generoso.salescatalog.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.time.Duration
import java.time.Instant

class HelloWorldControllerTest {

    private val controller = HelloWorldController()

    @Test
    fun helloWorldPublic_shouldReturnsOk() {
        val response = controller.helloWorldPublic()
        assertThat(response.statusCode.value()).isEqualTo(200)
        assertThat(response.body).isEqualTo("Hello")
    }

    @Test
    fun helloWorld_shouldReturnsOk() {
        val jwt = generateJwt()
        val jwtToken = JwtAuthenticationToken(jwt)

        val response = controller.helloWorld(jwtToken)
        assertThat(response.statusCode.value()).isEqualTo(200)
        assertThat(response.body).isEqualTo("Hello user!")
    }

    @Test
    fun helloWorldClient_shouldReturnsOk() {
        val response = controller.helloWorldClient()
        assertThat(response.statusCode.value()).isEqualTo(200)
        assertThat(response.body).isEqualTo("Hello client!")
    }

    @Test
    fun helloWorldSales_shouldReturnsOk() {
        val response = controller.helloWorldSales()
        assertThat(response.statusCode.value()).isEqualTo(200)
        assertThat(response.body).isEqualTo("Hello sales!")
    }

    private fun generateJwt(): Jwt {
        val now = Instant.now()
        return Jwt(
            "token",
            now,
            now.plus(Duration.ofMinutes(10)),
            mapOf("header1" to "value"),
            mapOf("preferred_username" to "user")
        )
    }
}