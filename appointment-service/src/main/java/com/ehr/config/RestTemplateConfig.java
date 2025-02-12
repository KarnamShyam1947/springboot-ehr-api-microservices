package com.ehr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    
    @Value("${application.service-url.user-service}")
    private String userServiceBaseServiceUrl;

    @Bean(name = "user-service")
    RestTemplate getUserServiceRestTemplate() {
        return new RestTemplateBuilder()
                .rootUri(userServiceBaseServiceUrl)
                .build();
    }
    
    RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

}
