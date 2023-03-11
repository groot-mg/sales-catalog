package com.generoso.ft.salescatalog.config

import com.generoso.salescatalog.SalesCatalogApplication
import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Profile
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy

@Profile("local")
@Configuration
@DependsOn("localWiremockServer")
class LocalSalesCatalogServer {
    private var sdApplicationContext: ConfigurableApplicationContext? = null

    @PostConstruct
    fun startUp() {
        sdApplicationContext = SpringApplication.run(SalesCatalogApplication::class.java)
    }

    @PreDestroy
    fun shutDown() {
        sdApplicationContext!!.close()
    }
}
