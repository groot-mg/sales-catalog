package com.generoso.salescatalog.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloWorld {

    @GetMapping("/hello-world")
    fun helloWorld(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello world!")
    }
}