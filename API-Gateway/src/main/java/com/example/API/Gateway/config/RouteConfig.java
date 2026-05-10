package com.example.API.Gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("customer-service-route", r -> r.path("/api/v1/customers/**")
                        .uri("lb://customer-service"))
                .route("asset-service-route", r -> r.path("/api/v1/assets/**")
                        .uri("lb://asset-service"))
                .route("contract-service-route", r -> r.path("/api/v1/contracts/**")
                        .uri("lb://contract-service"))
                .route("bill-service-route", r -> r.path("/api/v1/bills/**")
                        .uri("lb://bill-service"))
                .build();
    }
}