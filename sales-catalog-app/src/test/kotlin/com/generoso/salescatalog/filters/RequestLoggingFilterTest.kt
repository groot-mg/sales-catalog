package com.generoso.salescatalog.filters

import ch.qos.logback.classic.Level
import com.generoso.salescatalog.LogUtils.assertMessageWasInLogs
import com.generoso.salescatalog.LogUtils.getListAppenderForClass
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockFilterChain
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse


class RequestLoggingFilterTest {

    @Test
    fun `filter should include log lines for an usual request without queryString`() {
        // Arrange
        val listAppender = getListAppenderForClass(RequestLoggingFilter::class.java)
        val filter = RequestLoggingFilter()
        val request = MockHttpServletRequest()
        request.method = "GET"
        request.requestURI = "/unit-test"
        val response = MockHttpServletResponse()
        response.status = 200

        // Act
        filter.doFilter(request, response, MockFilterChain())

        // Assert
        assertMessageWasInLogs(listAppender, "Incoming request GET /unit-test", Level.INFO)
        assertMessageWasInLogs(listAppender, "Returning request with status code: " + response.status, Level.INFO)
    }

    @Test
    fun `filter should include log lines for an usual request with queryString`() {
        // Arrange
        val listAppender = getListAppenderForClass(RequestLoggingFilter::class.java)
        val filter = RequestLoggingFilter()
        val request = MockHttpServletRequest()
        request.method = "GET"
        request.requestURI = "/unit-test"
        request.queryString = "size=1"
        val response = MockHttpServletResponse()
        response.status = 200

        // Act
        filter.doFilter(request, response, MockFilterChain())

        // Assert
        assertMessageWasInLogs(listAppender, "Incoming request GET /unit-test?size=1", Level.INFO)
        assertMessageWasInLogs(listAppender, "Returning request with status code: " + response.status, Level.INFO)
    }
}

