package com.generoso.ft.salescatalog.config

import com.generoso.salescatalog.SalesCatalogApplication
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Profile

@Profile("local")
@Configuration
@DependsOn("localWiremockServer")
class LocalSalesCatalogServer @Autowired constructor(
    @Value("\${spring.profiles.active}") private val profile: String
) {

    private var sdApplicationContext: ConfigurableApplicationContext? = null

    @PostConstruct
    fun startUp() {
        System.setProperty("spring.profiles.active", profile)
        sdApplicationContext = SpringApplication.run(SalesCatalogApplication::class.java)
    }

    @PreDestroy
    fun shutDown() {
        sdApplicationContext!!.close()
    }
}
