package com.generoso.ft.sd.steps;

import com.generoso.ft.sd.client.model.Apps;
import com.generoso.ft.sd.client.model.JsonMapper;
import com.generoso.ft.sd.state.ScenarioState;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ResponseStepDefinitions {

    private final ScenarioState scenarioState;
    private final JsonMapper jsonMapper;

    @Then("the response status code should be {int}")
    public void theResponseCode(int expectedResponseCode) {
        assertEquals(expectedResponseCode, scenarioState.getActualResponse().statusCode());
    }

    @And("the app list should be empty")
    public void appListShouldBeEmpty() {
        var response = scenarioState.getActualResponse();
        var responseObj = jsonMapper.fromJson(response.body(), Apps.class);

        assertTrue(responseObj.applications().application().isEmpty());
    }

    @And("the app list contains an app called {word}")
    public void appListContainsTheApp(String appName) {
        var response = scenarioState.getActualResponse();
        var responseObj = jsonMapper.fromJson(response.body(), Apps.class);

        assertFalse(responseObj.applications().application().isEmpty());

        var applications = responseObj.applications().application();
        var firstApp = applications.get(0);
        assertEquals(appName, firstApp.name());
    }
}
