package com.generoso.ft.salescatalog.client

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.IOException
import java.net.http.HttpClient
import java.net.http.HttpResponse

@Component
class Client @Autowired constructor(private val httpClient: HttpClient) {

    fun execute(requestTemplate: RequestTemplate): HttpResponse<String> {
        val httpRequest = requestTemplate.newHttpRequest()
        return try {
            httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }
    }
}
