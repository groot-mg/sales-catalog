package com.generoso.ft.sd.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.generoso.ft.sd.client.Client;
import com.generoso.ft.sd.client.RequestTemplate;
import com.generoso.ft.sd.client.model.Endpoint;
import com.generoso.ft.sd.client.model.JsonMapper;
import com.generoso.ft.sd.client.model.PrivateHealthResponse;
import com.generoso.ft.sd.state.ScenarioState;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PrivateStepDefinitions {

    private final Map<Endpoint, RequestTemplate> privateRequestTemplates;
    private final Client client;
    private final ScenarioState scenarioState;
    private final JsonMapper jsonMapper;
    private final ObjectMapper objectMapper;

    @Given("a private endpoint {} is prepared")
    public void thePrivateEndpointIsPrepared(Endpoint endpoint) {
        RequestTemplate requestTemplate = getRequestTemplate(endpoint);
        scenarioState.setRequestTemplate(requestTemplate);
    }

    @When("the request is sent")
    public void theEndpointReceivesARequest() {
        HttpResponse<String> response = client.execute(scenarioState.getRequestTemplate());
        scenarioState.setActualResponse(response);
    }

    @Then("the response status code should be {int}")
    public void theResponseCode(int expectedResponseCode) {
        assertEquals(expectedResponseCode, scenarioState.getActualResponse().statusCode());
    }

    @Then("the health response body of the message should have the status {string}")
    public void theBodyOfTheMessageContains(String expectedResponseBody) {
        var response = scenarioState.getActualResponse();
        var responseObj = jsonMapper.fromJson(response.body(), PrivateHealthResponse.class);
        assertThat(responseObj.status()).isEqualTo(expectedResponseBody);
    }

    @Then("health components should contain the status {word}:")
    public void healthComponentsShouldContainTheStatus(String status, List<String> componentNames) throws JsonProcessingException {
        JsonNode jsonResponse = objectMapper.readTree(scenarioState.getActualResponse().body());
        JsonNode components = jsonResponse.get("components");

        componentNames.forEach(componentName ->
                assertThat(components.get(componentName).get("status").asText()).isEqualTo(status));
    }

    private RequestTemplate getRequestTemplate(Endpoint endpoint) {
        if (privateRequestTemplates.containsKey(endpoint)) {
            return privateRequestTemplates.get(endpoint);
        }

        throw new RuntimeException("Invalid private request template provided.");
    }
}
