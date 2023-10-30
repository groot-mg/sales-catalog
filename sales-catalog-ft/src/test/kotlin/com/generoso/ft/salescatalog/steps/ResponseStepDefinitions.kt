package com.generoso.ft.salescatalog.steps

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.generoso.ft.salescatalog.state.ScenarioState
import com.generoso.salescatalog.dto.ProductV1Dto
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.And
import io.cucumber.java.en.Then
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal

class ResponseStepDefinitions @Autowired constructor(
    private val scenarioState: ScenarioState,
    private val mapper: ObjectMapper
) {

    @Then("the response status code should be {int}")
    fun theResponseCode(expectedResponseCode: Int) {
        assertEquals(expectedResponseCode, scenarioState.actualResponse?.statusCode())
    }

    @And("string response body should be {string}")
    fun stringResponseBodyShouldBe(body: String) {
        assertThat(scenarioState.actualResponseBody).isEqualTo(body)
    }

    @And("product response object has:")
    fun theProductRequestBodyIsSetTo(table: DataTable) {
        val responseObj = mapper.readValue(scenarioState.actualResponseBody, ProductV1Dto::class.java)

        val row: Map<String, String> = table.asMaps()[0]
        val price = row["price"]?.let { BigDecimal.valueOf(it.toLong()) }
        val quantity = row["quantity"]?.toLong()

        assertEquals(row["name"], responseObj.name)
        assertEquals(row["description"], responseObj.description)
        assertEquals(price, responseObj.price)
        assertEquals(quantity, responseObj.quantity)
        assertEquals(row["isReserved"].toBoolean(), responseObj.reserved)
        assertEquals(row["isSold"].toBoolean(), responseObj.sold)
    }
}
