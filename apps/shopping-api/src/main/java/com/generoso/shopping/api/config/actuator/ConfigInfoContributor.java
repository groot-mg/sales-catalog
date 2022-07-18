package com.generoso.shopping.api.config.actuator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ConfigInfoContributor implements InfoContributor {

    @Autowired
    private Environment env;

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("environment", env.getActiveProfiles().length > 0 ? env.getActiveProfiles()[0] : "default");
    }
}