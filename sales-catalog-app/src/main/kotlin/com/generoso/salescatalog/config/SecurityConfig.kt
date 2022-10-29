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

        http.authorizeRequests()
            .antMatchers("/hello-world2").hasAnyRole("api-client", "api-sales")
            .antMatchers("/hello-world3").hasRole("api-client")
            .antMatchers("/hello-world4").hasRole("api-sales")
            .antMatchers("/private/**").permitAll()
            .and()
            .authorizeRequests()
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
