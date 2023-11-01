package com.generoso.ft.salescatalog.client.model

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class JsonMapper @Autowired constructor(private val objectMapper: ObjectMapper) {

    init {
        objectMapper.registerModule(JavaTimeModule())
    }

    fun <T> toJson(obj: T): String = objectMapper.writeValueAsString(obj)

    fun <T> fromJson(json: String?, clazz: Class<T>?): T {
        return try {
            objectMapper.readValue(json, clazz)
        } catch (e: JsonProcessingException) {
            throw RuntimeException("Error reading json value", e)
        }
    }
}
