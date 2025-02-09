package com.ehr.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
    info = @Info(
        title = "User API",
        version = "1.0",
        description = "Spring Boot Rest API for User Service",
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
public class OpenAPIConfig {
    
}