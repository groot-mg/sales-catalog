package com.generoso.salescatalog.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain

const val ROLE_CLIENT = "api-client"
const val ROLE_SALES = "api-sales"

@Configuration
class SecurityConfig {

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        http.csrf().disable()
        http.authorizeHttpRequests()
            .requestMatchers("/hello-world-public").permitAll()
            .requestMatchers("/hello-world").hasAnyRole(ROLE_CLIENT, ROLE_SALES)
            .requestMatchers("/hello-world-client").hasRole(ROLE_CLIENT)
            .requestMatchers("/hello-world-sales").hasRole(ROLE_SALES)
            .requestMatchers("/private/**").permitAll()
            .and()
            .authorizeHttpRequests()
            .anyRequest()
            .authenticated()

        http.sessionManagement { session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        http.oauth2ResourceServer { config: OAuth2ResourceServerConfigurer<HttpSecurity> -> config.jwt() }
        return http.build()
    }

    @Bean
    fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        val jwtAuthenticationConverter = JwtAuthenticationConverter()
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(CustomJwtGrantedAuthoritiesConverter())
        return jwtAuthenticationConverter
    }
}
