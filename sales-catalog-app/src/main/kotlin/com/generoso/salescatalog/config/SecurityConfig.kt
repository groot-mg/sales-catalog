package com.generoso.salescatalog.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        http.csrf().disable()
        http.authorizeHttpRequests()
            .requestMatchers("/hello-world2").hasAnyRole("api-client", "api-sales")
            .requestMatchers("/hello-world3").hasRole("api-client")
            .requestMatchers("/hello-world4").hasRole("api-sales")
            .requestMatchers("/private/**").permitAll()
            .requestMatchers("/test").hasRole("api-client")
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
