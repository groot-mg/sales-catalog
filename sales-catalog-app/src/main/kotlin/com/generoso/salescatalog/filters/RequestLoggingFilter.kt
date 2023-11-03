package com.generoso.salescatalog.filters

import jakarta.servlet.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import java.io.IOException

class RequestLoggingFilter : Filter {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpServletRequest = request as HttpServletRequest
        val method = httpServletRequest.method
        val path = httpServletRequest.requestURI
        val queryString = httpServletRequest.queryString
        log.info("Incoming request {} {}", method, if (queryString.isNullOrEmpty()) path else "$path?$queryString")
        chain.doFilter(request, response)
        log.info("Returning request with status code: {}", (response as HttpServletResponse).status)
    }
}

