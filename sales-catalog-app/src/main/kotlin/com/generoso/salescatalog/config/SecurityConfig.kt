package com.generoso.salescatalog.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain


@Configuration
class SecurityConfig {

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        http.csrf().disable()
        http.authorizeRequests()
            .antMatchers("/private/**").permitAll()
            .and()
            .authorizeRequests()
            .anyRequest()
            .authenticated()
        http.sessionManagement { session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        http.oauth2ResourceServer { config: OAuth2ResourceServerConfigurer<HttpSecurity> -> config.jwt() }
        return http.build()
    }
}
