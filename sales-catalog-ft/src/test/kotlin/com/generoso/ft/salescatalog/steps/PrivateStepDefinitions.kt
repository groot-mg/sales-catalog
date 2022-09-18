package com.generoso.ft.salescatalog.steps

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.generoso.ft.salescatalog.client.RequestTemplate
import com.generoso.ft.salescatalog.client.model.Endpoint
import com.generoso.ft.salescatalog.client.model.JsonMapper
import com.generoso.ft.salescatalog.client.model.PrivateHealthResponse
import com.generoso.ft.salescatalog.state.ScenarioState
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
import org.springframework.beans.factory.annotation.Autowired
import java.util.function.Consumer

class PrivateStepDefinitions @Autowired constructor(
    private val privateRequestTemplates: Map<Endpoint, RequestTemplate>,
    private val scenarioState: ScenarioState,
    private val jsonMapper: JsonMapper,
    private val objectMapper: ObjectMapper
) {

    @Given("a private endpoint {} is prepared")
    fun thePrivateEndpointIsPrepared(endpoint: Endpoint) {
        scenarioState.requestTemplate = getRequestTemplate(endpoint)
    }

    @Then("the health response body of the message should have the status {string}")
    fun theHealthResponseBodyOfTheMessageShouldHaveTheStatus(expectedResponseBody: String) {
        val responseObj = jsonMapper.fromJson(scenarioState.actualResponseBody, PrivateHealthResponse::class.java)
        assertThat(responseObj.status).isEqualTo(expectedResponseBody)
    }

    @Then("health components should contain the status {word}:")
    @Throws(JsonProcessingException::class)
    fun healthComponentsShouldContainTheStatus(status: String?, componentNames: List<String?>) {
        val jsonResponse = objectMapper.readTree(scenarioState.actualResponseBody)
        val components = jsonResponse["components"]
        componentNames.forEach(Consumer { componentName: String? ->
            assertThat(components[componentName]["status"].asText()).isEqualTo(status)
        })
    }

    @Then("the body of the message contains {string}")
    fun theBodyOfTheMessageContains(expectedResponseBodyContent: String) {
        assertTrue(scenarioState.actualResponseBody.contains(expectedResponseBodyContent))
    }

    @Then("it should return (.*) information containing the following keys and values:$")
    @Throws(JsonProcessingException::class)
    fun theInfoContains(key: String, expectedInfo: Map<String, String>) {
        val jsonResponse = objectMapper.readTree(scenarioState.actualResponseBody)
        val node = if (key == "java") jsonResponse["app"] else jsonResponse
        for (entry in expectedInfo.entries.iterator()) {
            val actualInfo = node[key][entry.key].asText()
            assertThat(actualInfo).matches(entry.value)
        }
    }

    @Then("the response body contains:")
    @Throws(JsonProcessingException::class)
    fun theResponseBodyContains(keys: List<String>) {
        val jsonResponse: JsonNode = objectMapper.readTree(scenarioState.actualResponseBody)
        keys.forEach(Consumer { keyName: String ->
            assertTrue(jsonResponse.has(keyName))
        })
    }

    private fun getRequestTemplate(endpoint: Endpoint): RequestTemplate? {
        if (privateRequestTemplates.containsKey(endpoint)) {
            return privateRequestTemplates[endpoint]
        }

        throw RuntimeException("Request template not found: $endpoint")
    }
}
