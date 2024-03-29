package com.generoso.ft.salescatalog.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.generoso.ft.salescatalog.client.RequestTemplate
import com.generoso.ft.salescatalog.client.model.Endpoint
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.common.ClasspathFileSource
import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.common.filemaker.FilenameMaker
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.standalone.JsonFileMappingsSource
import io.zonky.test.db.postgres.embedded.EmbeddedPostgres
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.*
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
    @Profile("local-ft")
    fun localWiremockServer(
        @Value("\${wiremock.host}") host: String,
        @Value("\${wiremock.port}") port: Int,
        @Value("\${wiremock.enable-console-notifier}") enableConsoleNotifier: Boolean
    ): WireMockServer {
        return WireMockServer(
            WireMockConfiguration.wireMockConfig()
                .notifier(ConsoleNotifier(enableConsoleNotifier))
                .port(port).mappingSource(JsonFileMappingsSource(ClasspathFileSource("mappings"), FilenameMaker()))
        )
    }

    @Bean // close() is called by default on destroy object event
    @Profile("local-ft")
    fun embeddedPostgres(): EmbeddedPostgres = EmbeddedPostgres.builder().setPort(5432).start()
}