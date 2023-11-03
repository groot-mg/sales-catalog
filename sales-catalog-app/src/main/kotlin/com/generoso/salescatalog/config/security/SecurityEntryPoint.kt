package com.generoso.salescatalog.config.security

import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class SecurityEntryPoint : AuthenticationEntryPoint {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Throws(IOException::class, ServletException::class)
    override fun commence(
        request: HttpServletRequest, response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        if (response.status == 401) {
            // Spring calls this method twice, so only handles it to log on the second call
            log.info("Unauthorized access detected: " + authException.message)
        }

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
    }
}