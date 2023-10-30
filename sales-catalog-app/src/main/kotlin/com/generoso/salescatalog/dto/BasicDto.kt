package com.generoso.salescatalog.dto

import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
abstract class BasicDto(var id: UUID? = null, val version: String? = null)