package com.generoso.ft.salescatalog.steps

import com.generoso.ft.salescatalog.client.model.JsonMapper
import com.generoso.ft.salescatalog.state.ScenarioState
import com.generoso.salescatalog.dto.ProductV1Dto
import com.generoso.salescatalog.exception.error.ValidationErrorDetails
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.And
import io.cucumber.java.en.Then
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal

class ResponseStepDefinitions @Autowired constructor(
    private val scenarioState: ScenarioState,
    private val mapper: JsonMapper
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
        val responseObj = mapper.fromJson(scenarioState.actualResponseBody, ProductV1Dto::class.java)

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

    @And("response error body should contain the field {} and message {}")
    fun responseErrorBodyShouldContainTheFieldAndMessage(field: String, message: String) {
        val errorResponse = mapper.fromJson(scenarioState.actualResponseBody, ValidationErrorDetails::class.java)
        val validations = errorResponse.validations
        assertThat(validations).isNotEmpty()
        message.split(", ").forEach { m ->
            val messages = validations?.filter { it.field == field }?.flatMap { it.messages.toList() }?.toTypedArray()
            assertThat(messages).contains(m)
        }
    }
}
