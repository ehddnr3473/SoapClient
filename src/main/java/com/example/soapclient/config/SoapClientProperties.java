package com.example.soapclient.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "soap")
public record SoapClientProperties(Endpoint calculator,
                                   Endpoint countryInfo,
                                   Duration connectTimeout,
                                   Duration readTimeout) {
    public record Endpoint(String url) {}
}
