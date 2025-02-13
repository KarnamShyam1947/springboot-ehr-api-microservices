package com.ehr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

// @Configuration
public class RestTemplateConfig {
    
    @Value("${application.service-url.user-service}")
    private String userServiceBaseServiceUrl;
    
    @Value("${application.service-url.appointment-service}")
    private String appointmentServiceBaseServiceUrl;

    // @Bean(name = "user-service", value = "user-service")
    RestTemplate getUserServiceRestTemplate() {
        return new RestTemplateBuilder()
                .rootUri(userServiceBaseServiceUrl)
                .build();
    }
    
    // @Bean(name = "appointment-service", value = "appointment-service")
    RestTemplate getAppointmentServiceRestTemplate() {
        return new RestTemplateBuilder()
                .rootUri(appointmentServiceBaseServiceUrl)
                .build();
    }
    
    @Primary
    RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

}
