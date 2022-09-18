package com.generoso.ft.salescatalog.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.generoso.ft.salescatalog.client.RequestTemplate
import com.generoso.ft.salescatalog.client.model.Endpoint
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import java.net.http.HttpClient
import java.util.function.Consumer

@Configuration
@ComponentScan("com.generoso.ft.salescatalog.*")
class TestConfiguration {

    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper()
    }

    @Bean
    fun httpClient(): HttpClient {
        return HttpClient.newBuilder().build()
    }

    @Bean
    fun privateRequestTemplates(
        @Qualifier("private") templates: List<RequestTemplate>
    ): Map<Endpoint, RequestTemplate> {
        val map: HashMap<Endpoint, RequestTemplate> = HashMap(templates.size)
        templates.forEach(Consumer { t: RequestTemplate ->
            map[t.endpoint] = t
        })
        return map
    }

    @Bean
    fun requestTemplates(
        @Qualifier("service-request") templates: List<RequestTemplate>
    ): Map<Endpoint, RequestTemplate> {
        val map: HashMap<Endpoint, RequestTemplate> = HashMap(templates.size)
        templates.forEach(Consumer { t: RequestTemplate ->
            map[t.endpoint] = t
        })
        return map
    }
}