package com.generoso.ft.gateway.client.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum Endpoint {

    PRIVATE_INFO("/private/info", "GET"),
    PRIVATE_HEALTH_CHECK("/private/health", "GET"),
    PRIVATE_METRICS("/private/metrics", "GET"),
    GET_APPS("/eureka/apps", "GET"),
    ADD_APP("/eureka/apps", "POST");

    private final String path;
    private final String method;;
}