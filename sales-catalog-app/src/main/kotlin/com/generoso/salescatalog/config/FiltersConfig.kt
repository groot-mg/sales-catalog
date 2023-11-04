package com.generoso.salescatalog.config

import com.generoso.salescatalog.filters.RequestLoggingFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FiltersConfig {
    @Bean
    fun incomingRequestLogFilter(): FilterRegistrationBean<RequestLoggingFilter> =
        FilterRegistrationBean(RequestLoggingFilter()).apply { order = 0 }
}

