package com.alexandrov.config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config/config.properties")
public interface CredentialsConfig extends Config {

    String host();
}