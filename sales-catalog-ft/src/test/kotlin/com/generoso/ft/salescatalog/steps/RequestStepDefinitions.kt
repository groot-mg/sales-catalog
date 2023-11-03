package com.generoso.ft.salescatalog.steps

import com.generoso.ft.salescatalog.client.Client
import com.generoso.ft.salescatalog.client.RequestTemplate
import com.generoso.ft.salescatalog.client.model.Endpoint
import com.generoso.ft.salescatalog.client.model.JsonMapper
import com.generoso.ft.salescatalog.state.ScenarioState
import com.generoso.salescatalog.dto.ProductV1Dto
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.When
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal

class RequestStepDefinitions @Autowired constructor(
    private val requestTemplates: Map<Endpoint, RequestTemplate>,
    private val client: Client,
    private val scenarioState: ScenarioState,
    private val mapper: JsonMapper
) {

    companion object {
        private const val ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        private const val PRODUCT_NAME_MIN_LENGTH = 5
        private const val PRODUCT_NAME_MAX_LENGTH = 100
        private const val PRODUCT_DESCRIPTION_MAX_LENGTH = 256
    }

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

    @And("the product request body has {word}")
    fun theProductRequestBodyIsSetTo(scenario: String) {
        val dto = ProductV1Dto().apply {
            name = "Product Name"
            description = "Product description"
            price = BigDecimal.valueOf(10)
            quantity = 50
        }

        when (scenario) {
            "NAME_EMPTY" -> dto.name = ""
            "NAME_MISSING" -> dto.name = null
            "NAME_SHORTER_THAN_MIN_LENGTH_ALLOWED" -> dto.name = generateRandomString(PRODUCT_NAME_MIN_LENGTH - 1)
            "NAME_MIN_LENGTH_ALLOWED" -> dto.name = generateRandomString(PRODUCT_NAME_MIN_LENGTH)
            "NAME_MAX_LENGTH_ALLOWED" -> dto.name = generateRandomString(PRODUCT_NAME_MAX_LENGTH)
            "NAME_LONGER_THAN_ALLOWED" -> dto.name = generateRandomString(PRODUCT_NAME_MAX_LENGTH + 1)
            "DESCRIPTION_LONGER_THAN_ALLOWED" -> dto.description = generateRandomString(PRODUCT_DESCRIPTION_MAX_LENGTH + 1)
            "DESCRIPTION_MAX_LENGTH_ALLOWED" -> dto.description = generateRandomString(PRODUCT_DESCRIPTION_MAX_LENGTH)
            "DESCRIPTION_EMPTY" -> dto.description = ""
            "DESCRIPTION_MISSING" -> dto.description = null
            "PRICE_MISSING" -> dto.price = null
            "PRICE_NEGATIVE" -> dto.price = BigDecimal.valueOf(-1)
            "PRICE_ZERO" -> dto.price = BigDecimal.valueOf(0)
            "PRICE_DIGITS_MORE_THAN_ALLOWED" -> dto.price = BigDecimal.valueOf(10000000000)
            "PRICE_DECIMAL_DIGITS_MORE_THAN_ALLOWED" -> dto.price = BigDecimal.valueOf(1000000000.111)
            "PRICE_MAX_DIGITS_ALLOWED" -> dto.price = BigDecimal.valueOf(10000000)
            "PRICE_DECIMAL_MAX_DIGITS_ALLOWED" -> dto.price = BigDecimal.valueOf(10000000.11)
            "QUANTITY_MISSING" -> dto.quantity = null
            "QUANTITY_NEGATIVE" -> dto.quantity = -1
            "QUANTITY_ZERO" -> dto.quantity = 0
            "QUANTITY_POSITIVE" -> dto.quantity = 1
            "ALL_FIELDS_INVALID" -> dto.apply {
                name = null
                description = generateRandomString(PRODUCT_DESCRIPTION_MAX_LENGTH + 1)
                price = BigDecimal.valueOf(-1)
                quantity = -1
            }
        }

        scenarioState.requestTemplate?.body(mapper.toJson(dto))
    }

    private fun getRequestTemplate(endpoint: Endpoint): RequestTemplate? {
        if (requestTemplates.containsKey(endpoint)) {
            return requestTemplates[endpoint]
        }
        throw RuntimeException("Invalid request template provided.")
    }

    private fun generateRandomString(length: Int): String = (1..length).map { ALLOWED_CHARACTERS.random() }.joinToString("")

}
