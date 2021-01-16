package com.eapplication.eapplicationback.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class EApplicationConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().components(new Components()).info(new Info().title("Contact Application API")
                .description("Spring Boot RESTful service using springdoc-openapi and OpenAPI 3."));
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
