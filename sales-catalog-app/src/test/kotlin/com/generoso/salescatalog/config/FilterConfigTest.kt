package com.generoso.salescatalog.config

import com.generoso.salescatalog.filters.RequestLoggingFilter
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [FiltersConfig::class])
class FilterConfigTest {

    @Autowired
    private lateinit var requestLoggingFilter: FilterRegistrationBean<RequestLoggingFilter>

    @Test
    fun shouldCreateFiltersInCorrectOrder() {
        Assertions.assertThat(requestLoggingFilter.order).isZero()
    }
}

