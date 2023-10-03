package com.generoso.salescatalog.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.info.BuildProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfiguration {

    @Autowired
    private lateinit var buildProperties: BuildProperties

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI().info(apiInfo())
    }

    private fun apiInfo(): Info {
        return Info()
            .title("Sales Catalog Service")
            .description("Manage products and stock")
            .version(buildProperties.version)
            .contact(apiContact())
            .license(apiLicence())
    }

    private fun apiLicence(): License {
        return License()
            .name("MIT Licence")
            .url("https://opensource.org/licenses/mit-license.php")
    }

    private fun apiContact(): Contact {
        return Contact()
            .name("Mauricio Generoso")
            .email("mauriciomarquesgeneroso@gmail.com")
            .url("https://github.com/mauriciogeneroso")
    }
}