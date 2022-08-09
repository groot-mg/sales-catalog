package com.generoso.ft.gateway.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Application(@JsonProperty("versions__delta") String version,
                          @JsonProperty("apps__hashcode") String appHashCode,
                          List<ApplicationDetail> application) {
}
