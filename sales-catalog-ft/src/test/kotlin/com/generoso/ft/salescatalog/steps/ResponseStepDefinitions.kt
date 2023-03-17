package com.generoso.ft.salescatalog.steps

import com.generoso.ft.salescatalog.state.ScenarioState
import io.cucumber.java.en.And
import io.cucumber.java.en.Then
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired

class ResponseStepDefinitions @Autowired constructor(
    private val scenarioState: ScenarioState
) {

    @Then("the response status code should be {int}")
    fun theResponseCode(expectedResponseCode: Int) {
        assertEquals(expectedResponseCode, scenarioState.actualResponse?.statusCode())
    }

    @And("string response body should be {string}")
    fun stringResponseBodyShouldBe(body: String) {
        assertThat(scenarioState.actualResponseBody).isEqualTo(body)
    }
}
