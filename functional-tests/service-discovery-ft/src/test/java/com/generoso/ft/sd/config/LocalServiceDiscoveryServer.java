package com.generoso.ft.sd.config;

import com.generoso.sd.ServiceDiscoveryServerApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Profile("local")
@Configuration
public class LocalServiceDiscoveryServer {

    private ConfigurableApplicationContext sdApplicationContext;

    @PostConstruct
    public void startUp() {
        sdApplicationContext = SpringApplication.run(ServiceDiscoveryServerApplication.class);
    }

    @PreDestroy
    public void shutDown() {
        sdApplicationContext.close();
    }
}