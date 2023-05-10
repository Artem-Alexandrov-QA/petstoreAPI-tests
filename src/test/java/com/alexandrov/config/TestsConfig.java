package com.alexandrov.config;

import org.aeonbits.owner.Config;

public interface TestsConfig extends Config {

    @Key("baseUrl")
    @DefaultValue("https://petstore.swagger.io/")
    String getBaseUrl();
}