package com.generoso.ft.salescatalog.config

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy

@Configuration
@Profile("local")
class LocalWiremockServer {
    private val log = LoggerFactory.getLogger(this.javaClass)

    private var wireMockServer: WireMockServer? = null

    @Value(value = "\${wiremock.host}")
    private val host: String? = null

    @Value(value = "\${wiremock.port}")
    private val port = 0
    @PostConstruct
    fun startUp() {
        log.info("Trying to start a wiremock instance for: {}. Port: {}", host, port)
        wireMockServer = WireMockServer(WireMockConfiguration.options().bindAddress(host).port(port))
        wireMockServer!!.start()
        log.info("Wiremock is running")
    }

    @PreDestroy
    fun shutDown() {
        log.info("Wiremock is shutting down")
        WireMock.configureFor(port)
        WireMock.shutdownServer()
        log.info("Wiremock server is down")
    }
}
