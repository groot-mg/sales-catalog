package com.generoso.salescatalog.controller


import com.generoso.salescatalog.SalesCatalogApplication
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseType
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [SalesCatalogApplication::class],
    useMainMethod = SpringBootTest.UseMainMethod.WHEN_AVAILABLE
)
@ActiveProfiles("unit-tests")
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
@AutoConfigureObservability
@AutoConfigureEmbeddedDatabase(type = DatabaseType.POSTGRES, provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY)
class ActuatorEndpointTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @ParameterizedTest
    @CsvSource(
        "/private",
        "/private/info",
        "/private/health",
        "/private/metrics",
        "/private/api-docs",
        "/private/swagger-ui/index.html",
    )
    fun shouldExposeSpecifiedEndpoint(path: String) {
        mockMvc.perform(get(path)).andExpect(status().isOk)
    }

    @Test
    fun shouldReturnRedirectionToCustomSwaggerEndpoint() {
        mockMvc.perform(get("/private/swagger")).andExpect(status().is3xxRedirection)
    }
}