package com.stackroute.to_do_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    RouteLocator getRoutes(RouteLocatorBuilder builder){
        return builder.routes()
                .route(p ->p
                        .path("/to-do-auth-app/**")
                        .uri("lb://to-do-auth-app"))
                .route(p ->p
                        .path("/to-do-tracker-app/**")
                        .uri("lb://to-do-tracker-app"))
                .build();
    }
}
