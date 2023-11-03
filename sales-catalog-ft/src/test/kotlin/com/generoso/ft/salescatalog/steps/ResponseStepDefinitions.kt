package com.generoso.ft.salescatalog.steps

import com.generoso.ft.salescatalog.client.model.JsonMapper
import com.generoso.ft.salescatalog.model.ApiResponse
import com.generoso.ft.salescatalog.state.ScenarioState
import com.generoso.salescatalog.dto.ProductV1Dto
import com.generoso.salescatalog.exception.error.ErrorDetail
import com.generoso.salescatalog.exception.error.ValidationErrorDetails
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.And
import io.cucumber.java.en.Then
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired

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
        val price = row["price"]?.toBigDecimal()
        val quantity = row["quantity"]?.toLong()

        assertEquals(row["name"], responseObj.name)
        assertEquals(row["description"], responseObj.description)
        assertEquals(price, responseObj.price)
        assertEquals(quantity, responseObj.quantity)

        if (row.containsKey("isReserved")) {
            assertEquals(row["isReserved"].toBoolean(), responseObj.reserved)
        }

        if (row.containsKey("isSold")) {
            assertEquals(row["isSold"].toBoolean(), responseObj.sold)
        }
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

    @And("the response content is empty")
    fun theResponseContentIsEmpty() {
        val apiResponse = mapper.fromJson(scenarioState.actualResponseBody, ApiResponse::class.java)
        assertThat(apiResponse.content).isEmpty()
    }

    @And("the response content contains the product id {word}")
    fun theResponseContentContainsTheProductId(productId: String) {
        val apiResponse = mapper.fromJson(scenarioState.actualResponseBody, ApiResponse::class.java)
        assertThat(apiResponse.content.map { it.id.toString() }).contains(productId)
    }

    @And("^the pageable response contains (\\d+) (?:element|elements) and (\\d+) (?:page|pages)$")
    fun thePageableResponseContainsElementsAndPages(elements: Int, pages: Int) {
        val apiResponse = mapper.fromJson(scenarioState.actualResponseBody, ApiResponse::class.java)
        assertThat(apiResponse.totalElements).isEqualTo(elements)
        assertThat(apiResponse.totalPages).isEqualTo(pages)
    }

    @And("^the pageable response contains (\\d+) (?:element|elements) and page is (\\d+) and (\\d+) total elements and (\\d+) total pages$")
    fun thePageableResponseContainsElementsAndPagesAndTotalElementsAndTotalPages(elements: Int, pages: Int, totalElements: Int, totalPages: Int) {
        val apiResponse = mapper.fromJson(scenarioState.actualResponseBody, ApiResponse::class.java)
        assertThat(apiResponse.numberOfElements).isEqualTo(elements)
        assertThat(apiResponse.number).isEqualTo(pages)
        assertThat(apiResponse.totalElements).isEqualTo(totalElements)
        assertThat(apiResponse.totalPages).isEqualTo(totalPages)
    }

    @And("the response error detail should be {}")
    fun theResponseErrorDetailShouldBe(errorDetail: String) {
        val errorResponse = mapper.fromJson(scenarioState.actualResponseBody, ErrorDetail::class.java)
        assertThat(errorResponse.detail).isEqualTo(errorDetail)
    }

    @And("products response should be on the order {word}")
    fun productsResponseShouldBeOnTheOrder(order: String) {
        val apiResponse = mapper.fromJson(scenarioState.actualResponseBody, ApiResponse::class.java)
        val orderElements = order.split(",")
        assertThat(orderElements.size).isEqualTo(apiResponse.numberOfElements)

        for ((index, name) in orderElements.withIndex()) {
            assertThat(apiResponse.content[index].name).isEqualTo(name)
        }
    }
}
