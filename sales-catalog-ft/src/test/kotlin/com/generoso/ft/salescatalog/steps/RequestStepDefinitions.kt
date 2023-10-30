package com.generoso.ft.salescatalog.steps

import com.fasterxml.jackson.databind.ObjectMapper
import com.generoso.ft.salescatalog.client.Client
import com.generoso.ft.salescatalog.client.RequestTemplate
import com.generoso.ft.salescatalog.client.model.Endpoint
import com.generoso.ft.salescatalog.state.ScenarioState
import com.generoso.salescatalog.dto.ProductV1Dto
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.When
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal

class RequestStepDefinitions @Autowired constructor(
    private val requestTemplates: Map<Endpoint, RequestTemplate>,
    private val client: Client,
    private val scenarioState: ScenarioState,
    private val mapper: ObjectMapper
) {

    @Given("an endpoint {} is prepared")
    fun thePrivateEndpointIsPrepared(endpoint: Endpoint) {
        scenarioState.requestTemplate = getRequestTemplate(endpoint)
    }

    @Given("an endpoint {} is prepared with path parameter {word}")
    fun anEndpointIsPreparedWithPathParameter(endpoint: Endpoint, pathParameter: String?) {
        val requestTemplate: RequestTemplate? = getRequestTemplate(endpoint)
        requestTemplate?.pathParameter(pathParameter)
        scenarioState.requestTemplate = requestTemplate
    }

    @When("the request is sent")
    fun theEndpointReceivesARequest() {
        val response = client.execute(scenarioState.requestTemplate!!)
        scenarioState.actualResponse = response
    }

    @And("the product request body is set to:")
    fun theProductRequestBodyIsSetTo(table: DataTable) {
        val row: Map<String, String> = table.asMaps()[0]
        val name = row["name"]
        val description = row["description"]
        val price = row["price"]?.let { BigDecimal.valueOf(it.toLong()) }
        val quantity = row["quantity"]?.toLong()

        val dto = ProductV1Dto()
        dto.name = name
        dto.description = description
        dto.price = price
        dto.quantity = quantity

        scenarioState.requestTemplate?.body(mapper.writeValueAsString(dto))
    }

    private fun getRequestTemplate(endpoint: Endpoint): RequestTemplate? {
        if (requestTemplates.containsKey(endpoint)) {
            return requestTemplates[endpoint]
        }
        throw RuntimeException("Invalid request template provided.")
    }
}
