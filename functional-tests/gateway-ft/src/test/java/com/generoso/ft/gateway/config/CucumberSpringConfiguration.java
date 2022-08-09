package com.generoso.ft.gateway.config;

import com.generoso.ft.gateway.YamlFileApplicationContextInitializer;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
        TestConfiguration.class,
        LocalGatewayServer.class,
        LocalWiremockServer.class
},
        initializers = YamlFileApplicationContextInitializer.class)
@CucumberContextConfiguration
public class CucumberSpringConfiguration {
}