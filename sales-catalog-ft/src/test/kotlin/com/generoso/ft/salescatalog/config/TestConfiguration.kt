package com.generoso.ft.salescatalog.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.generoso.ft.salescatalog.client.RequestTemplate
import com.generoso.ft.salescatalog.client.model.Endpoint
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.common.ClasspathFileSource
import com.github.tomakehurst.wiremock.common.filemaker.FilenameMaker
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.standalone.JsonFileMappingsSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
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

    @Bean(initMethod = "start", destroyMethod = "stop")
    @Profile("local")
    fun localWiremockServer(
        @Value("\${wiremock.host}") host: String,
        @Value("\${wiremock.port}") port: Int
    ): WireMockServer {
        return WireMockServer(
            WireMockConfiguration.wireMockConfig()
//            .notifier(ConsoleNotifier(true))
                .port(port).mappingSource(JsonFileMappingsSource(ClasspathFileSource("mappings"), FilenameMaker()))
        )
    }
}