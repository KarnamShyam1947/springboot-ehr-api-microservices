package com.ehr.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
    info = @Info(
        title = "API Gateway",
        version = "1.0",
        description = "Spring Boot Rest API for User Applications",
        contact = @Contact(
            name = "Karnam Shyam",
            email = "karnamshyam9009@gmail.com",
            url = "https://karnamshyam1947.github.io/portfolio/"
        )
    )
    // security = {
    //     @SecurityRequirement(name = "bearerAuth")
    // }
)
// @SecurityScheme(
//         name = "bearerAuth",
//         description = "JWT authentication",
//         scheme = "bearer",
//         type = SecuritySchemeType.HTTP,
//         bearerFormat = "JWT",
//         in = SecuritySchemeIn.HEADER
// )
@Configuration
public class OpenAPIConfig {
    @Bean
 public RouteLocator routeLocator(RouteLocatorBuilder builder) {
  return builder
    .routes()
    .route(r -> r.path("/user-application-service/v3/api-docs").and().method(HttpMethod.GET).uri("lb://user-application-service"))
    .route(r -> r.path("/product-service/v3/api-docs").and().method(HttpMethod.GET).uri("lb://product-service"))
    .route(r -> r.path("/appointment-service/v3/api-docs").and().method(HttpMethod.GET).uri("lb://appointment-service"))
    .route(r -> r.path("/patient-record-service/v3/api-docs").and().method(HttpMethod.GET).uri("lb://patient-record-service"))
    .build();
 }
}