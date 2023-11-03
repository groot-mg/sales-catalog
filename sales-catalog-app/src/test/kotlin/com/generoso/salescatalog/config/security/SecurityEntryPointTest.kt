package com.generoso.salescatalog.config.security

import ch.qos.logback.classic.Level
import com.generoso.salescatalog.LogUtils.assertMessageWasInLogs
import com.generoso.salescatalog.LogUtils.getListAppenderForClass
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.authentication.AuthenticationServiceException

class SecurityEntryPointTest {

    @Test
    fun whenAuthenticationEntryPointIsCalled_shouldLogTheUnauthorisedAccess() {
        // Arrange
        val listAppender = getListAppenderForClass(SecurityEntryPoint::class.java)
        val response = MockHttpServletResponse()
        response.status = 401

        // Act
        SecurityEntryPoint().commence(MockHttpServletRequest(), response, AuthenticationServiceException("Oauth2-exception"))

        // Assert
        assertMessageWasInLogs(listAppender, "Unauthorized access detected: Oauth2-exception", Level.INFO)
    }
}