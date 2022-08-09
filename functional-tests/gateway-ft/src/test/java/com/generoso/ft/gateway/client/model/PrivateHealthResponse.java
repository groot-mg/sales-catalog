package com.generoso.ft.gateway.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PrivateHealthResponse(String status) {

}
