package com.generoso.ft.salescatalog.config

import com.generoso.ft.salescatalog.YamlFileApplicationContextInitializer
import io.cucumber.spring.CucumberContextConfiguration
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(
    classes = [TestConfiguration::class, LocalSalesCatalogServer::class],
    initializers = [YamlFileApplicationContextInitializer::class]
)
@CucumberContextConfiguration
class CucumberSpringConfiguration