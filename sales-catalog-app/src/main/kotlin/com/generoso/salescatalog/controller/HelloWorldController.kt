package com.generoso.salescatalog.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloWorldController {

    @GetMapping("/hello-world")
    fun helloWorld(): ResponseEntity<String> {
        val authentication = SecurityContextHolder.getContext().authentication
        val username = (authentication.principal as Jwt).claims["preferred_username"] as String
        return ResponseEntity.ok("Hello $username!")
    }

    @GetMapping("/hello-world2")
    fun helloWorld2(user: JwtAuthenticationToken): ResponseEntity<String> {
        val username = (user.principal as Jwt).claims["preferred_username"] as String
        return ResponseEntity.ok("Hello $username!")
    }

    @GetMapping("/hello-world3")
    fun helloWorld3(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello Client")
    }

    @GetMapping("/hello-world4")
    fun helloWorld4(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello Sales")
    }
}