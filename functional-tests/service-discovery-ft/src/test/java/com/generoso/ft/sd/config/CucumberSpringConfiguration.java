package com.generoso.ft.sd.config;

import com.generoso.ft.sd.YamlFileApplicationContextInitializer;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
        TestConfiguration.class,
        LocalServiceDiscoveryServer.class,
},
        initializers = YamlFileApplicationContextInitializer.class)
@CucumberContextConfiguration
public class CucumberSpringConfiguration {
}