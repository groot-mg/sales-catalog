package com.generoso.ft.gateway.client;

import com.generoso.ft.gateway.client.model.Endpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Qualifier("service-request")
public class AddAppRequestTemplate extends RequestTemplate {

    @Autowired
    public AddAppRequestTemplate(@Value("${service.host}") String host,
                                 @Value("${service.context-path:}") String contextPath) {
        super(host, contextPath);
    }

    @Override
    public Endpoint getEndpoint() {
        return Endpoint.ADD_APP;
    }


}
