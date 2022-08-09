package com.generoso.ft.gateway.config;

import com.generoso.gateway.GatewayApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Profile("local")
@Configuration
@DependsOn("localWiremockServer")
public class LocalGatewayServer {

    private ConfigurableApplicationContext sdApplicationContext;

    @PostConstruct
    public void startUp() {
        sdApplicationContext = SpringApplication.run(GatewayApplication.class);
    }

    @PreDestroy
    public void shutDown() {
        sdApplicationContext.close();
    }
}