package com.generoso.salescatalog.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloWorld {

    @GetMapping("/hello-world")
    fun helloWorld(): ResponseEntity<String> {
        val authentication = SecurityContextHolder.getContext().authentication
        val username = (authentication.principal as Jwt).claims["preferred_username"] as String
        return ResponseEntity.ok("Hello $username!")
    }
}