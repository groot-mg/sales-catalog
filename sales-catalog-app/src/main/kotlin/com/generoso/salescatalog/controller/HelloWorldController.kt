package com.generoso.salescatalog.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloWorldController {

    @GetMapping("/hello-world-public")
    fun helloWorldPublic(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello")
    }

    @GetMapping("/hello-world")
    fun helloWorld(user: JwtAuthenticationToken): ResponseEntity<String> {
        val username = (user.principal as Jwt).claims["preferred_username"] as String
        return ResponseEntity.ok("Hello $username!")
    }

    @GetMapping("/hello-world-client")
    fun helloWorldClient(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello client!")
    }

    @GetMapping("/hello-world-sales")
    fun helloWorldSales(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello sales!")
    }
}