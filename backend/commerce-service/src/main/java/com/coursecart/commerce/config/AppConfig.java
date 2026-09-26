package com.coursecart.commerce.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    /**
     * Provides a load-balanced WebClient.Builder that resolves service names
     * (e.g., "catalog-service", "enrollment-service") via Spring Cloud Consul.
     * Using WebClient instead of the deprecated RestTemplate for inter-service calls.
     */
    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
