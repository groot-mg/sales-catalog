package com.generoso.ft.salescatalog.client

import com.generoso.ft.salescatalog.client.model.Endpoint
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.net.URI
import java.net.URISyntaxException
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublisher

@Component
abstract class RequestTemplate protected constructor(private val host: String, private val contextPath: String) {
    private val log = LoggerFactory.getLogger(this.javaClass)

    private var pathParameter: String = ""
    private var body: String? = null

    protected var headers: MutableMap<String, String> = HashMap()

    init {
        defaultHeaders()
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


    fun defaultHeaders() {
        headers["Accept"] = "application/json"
        headers["Content-Type"] = "application/json"
    }

    private fun buildUri(): URI {
        return try {
            val finalUri = host + contextPath + endpoint.path + pathParameter
            log.info("Building Uri: {}", finalUri)
            URI(finalUri)
        } catch (e: URISyntaxException) {
            throw RuntimeException(
                "Error creating uri: ${host}${contextPath}${endpoint.path}. " +
                        "Error message: ${e.message}"
            )
        }
    }

    private fun bodyPublisher(): BodyPublisher {
        return if (body == null) HttpRequest.BodyPublishers.noBody() else HttpRequest.BodyPublishers.ofString(body)
    }
}