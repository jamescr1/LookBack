package com.jamescr1.backend.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AlphaVantageClient {
    private final RestClient.Builder restClientBuilder;

    @Autowired
    public AlphaVantageClient(RestClient.Builder restClientBuilder) {
        this.restClientBuilder = restClientBuilder;
    }
}
