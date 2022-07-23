package com.generoso.ft.sd.client;

import com.generoso.ft.sd.client.model.Endpoint;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

@Slf4j
@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class RequestTemplate {

    private final String host;
    private final String contextPath;
    private Map<String, String> headers = new HashMap<>();
    private String body;

    public abstract Endpoint getEndpoint();

    public HttpRequest newHttpRequest() {
        var builder = HttpRequest.newBuilder();
        builder.uri(buildUri());
        builder.method(getEndpoint().getMethod(), bodyPublisher());
        headers.forEach(builder::header);
        return builder.build();
    }

    public RequestTemplate headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public RequestTemplate body(String body) {
        this.body = body;
        return this;
    }

    private URI buildUri() {
        try {
            log.info("{}{}{}", host, contextPath, getEndpoint().getPath());
            return new URI(host + contextPath + getEndpoint().getPath());
        } catch (URISyntaxException e) {
            throw new RuntimeException(format("Error creating uri: %s%s%s. Error message: %s", host, contextPath,
                    getEndpoint().getPath(), e.getMessage()));
        }
    }

    private HttpRequest.BodyPublisher bodyPublisher() {
        return body == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(body);
    }
}
