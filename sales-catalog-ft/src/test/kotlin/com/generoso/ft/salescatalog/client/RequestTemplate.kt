package com.generoso.ft.salescatalog.client

import com.generoso.ft.salescatalog.client.model.Endpoint
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.net.URI
import java.net.URISyntaxException
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublisher
import java.util.*

@Component
abstract class RequestTemplate protected constructor(private val host: String, private val contextPath: String) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    private val queryParameters: MutableList<Map.Entry<String, String>> = mutableListOf()
    private var pathParameter: String = ""
    private var body: String? = null

    protected var headers: MutableMap<String, String> = HashMap()

    init {
        initDefaults()
    }

    abstract val endpoint: Endpoint

    fun newHttpRequest(): HttpRequest {
        val builder = HttpRequest.newBuilder()
        builder.uri(buildUri())
        builder.method(endpoint.method, bodyPublisher())
        headers.forEach { (name: String, value: String) ->
            builder.header(name, value)
        }
        return builder.build()
    }

    fun withHeader(key: String, value: String): RequestTemplate {
        headers[key] = value
        return this
    }

    fun body(body: String): RequestTemplate {
        this.body = body
        return this
    }

    fun pathParameter(pathParameter: String?): RequestTemplate {
        this.pathParameter = "/$pathParameter"
        return this
    }

    fun getDefaultHeaders(): MutableMap<String, String> {
        return mutableMapOf(
            "Accept" to "application/json",
            "Content-Type" to "application/json"
        )
    }

    fun addQueryParameter(name: String, value: String) {
        queryParameters.removeIf { it.key == name }
        queryParameters.add(AbstractMap.SimpleEntry(name, value))
    }

    fun getBody(): String? {
        return null
    }

    fun reset() = initDefaults()

    private fun initDefaults() {
        headers = getDefaultHeaders()
        body = getBody()
    }

    private fun buildUri(): URI {
        return try {
            val queryParameters = generateQueryString()
            val finalUri = host + contextPath + endpoint.path + pathParameter + queryParameters
            log.info("Building Uri: {}", finalUri)
            URI(finalUri)
        } catch (e: URISyntaxException) {
            throw RuntimeException(
                "Error creating uri: ${host}${contextPath}${endpoint.path}. " +
                        "Error message: ${e.message}"
            )
        }
    }

    fun generateQueryString(): String {
        return if (queryParameters.isNotEmpty())
            "?" + queryParameters.joinToString("&") { "${it.key}=${it.value}" }
        else
            ""
    }

    private fun bodyPublisher(): BodyPublisher {
        return if (body == null) HttpRequest.BodyPublishers.noBody() else HttpRequest.BodyPublishers.ofString(body)
    }
}