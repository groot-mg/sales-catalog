package com.generoso.salescatalog.controller.security

import com.generoso.salescatalog.config.SecurityConfig.Companion.ROLE_CLIENT
import com.generoso.salescatalog.config.SecurityConfig.Companion.ROLE_SALES
import com.generoso.salescatalog.controller.HelloWorldController
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.lang.String.format

@Import(HelloWorldController::class)
@WebMvcTest(HelloWorldController::class)
class HelloWorldControllerSecurityTest : SecurityControllerSetup() {

    // /hello-world-public
    @Test
    fun whenEndpointHasNoProtection_shouldAllowTheRequestWithoutToken() {
        mockMvc.perform(get("/hello-world-public"))
            .andExpect(status().isOk)
            .andExpect(content().string("Hello"))
    }

    // hello-world
    @Test
    fun whenProtectedHelloWorldReceivesNoToken_shouldReturns401() {
        mockMvc.perform(get("/hello-world")).andExpect(status().isUnauthorized)
    }

    @Test
    fun whenProtectedHelloWorldReceivesUnauthorizedToken_shouldReturns403() {
        mockMvc.perform(
            get("/hello-world")
                .header("Authorization", format("Bearer %s", generateJWT(listOf("api-random"))))
        ).andExpect(status().isForbidden)
    }

    @Test
    fun whenProtectedHelloWorldReceivesClientToken_shouldReturns200() {
        mockMvc.perform(
            get("/hello-world")
                .header("Authorization", format("Bearer %s", generateJWT("client", listOf(ROLE_CLIENT))))
        ).andExpect(status().isOk).andExpect(content().string("Hello client!"))
    }

    @Test
    fun whenProtectedHelloWorldReceivesSalesToken_shouldReturns200() {
        mockMvc.perform(
            get("/hello-world")
                .header("Authorization", format("Bearer %s", generateJWT("sales", listOf(ROLE_SALES))))
        ).andExpect(status().isOk).andExpect(content().string("Hello sales!"))
    }

    // hello-world-client
    @Test
    fun whenProtectedHelloWorldClientReceivesNoToken_shouldReturns401() {
        mockMvc.perform(get("/hello-world-client")).andExpect(status().isUnauthorized)
    }

    @Test
    fun whenProtectedHelloWorldClientReceivesUnauthorizedToken_shouldReturns403() {
        mockMvc.perform(
            get("/hello-world-client")
                .header("Authorization", format("Bearer %s", generateJWT("Random", listOf("api-random"))))
        ).andExpect(status().isForbidden)
    }

    @Test
    fun whenProtectedHelloWorldClientReceivesClientToken_shouldReturns200() {
        mockMvc.perform(
            get("/hello-world-client")
                .header("Authorization", format("Bearer %s", generateJWT("client", listOf(ROLE_CLIENT))))
        ).andExpect(status().isOk).andExpect(content().string("Hello client!"))
    }

    // hello-world-sales
    @Test
    fun whenProtectedHelloWorldSalesReceivesNoToken_shouldReturns401() {
        mockMvc.perform(get("/hello-world-sales")).andExpect(status().isUnauthorized)
    }

    @Test
    fun whenProtectedHelloWorldSalesReceivesUnauthorizedToken_shouldReturns403() {
        mockMvc.perform(
            get("/hello-world-sales")
                .header("Authorization", format("Bearer %s", generateJWT("Random", listOf("api-random"))))
        ).andExpect(status().isForbidden)
    }

    @Test
    fun whenProtectedHelloWorldSalesReceivesSalesToken_shouldReturns200() {
        mockMvc.perform(
            get("/hello-world-sales")
                .header("Authorization", format("Bearer %s", generateJWT("sales", listOf(ROLE_SALES))))
        ).andExpect(status().isOk).andExpect(content().string("Hello sales!"))
    }
}