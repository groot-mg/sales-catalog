package com.generoso.ft.salescatalog.client.model

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class JsonMapper @Autowired constructor(private val objectMapper: ObjectMapper) {

    fun <T> fromJson(json: String?, clazz: Class<T>?): T {
        return try {
            objectMapper.readValue(json, clazz)
        } catch (e: JsonProcessingException) {
            throw RuntimeException("Error reading json value", e)
        }
    }
}
