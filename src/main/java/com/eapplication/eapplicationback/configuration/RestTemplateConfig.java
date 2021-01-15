package com.eapplication.eapplicationback.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Configuration
public class RestTemplateConfig {

        @Bean
        public RestTemplate restTemplate(RestTemplateBuilder builder) {
            return new RestTemplate();

//            return builder
//                    .setConnectTimeout(Duration.ofSeconds(30))
//                    .setReadTimeout(Duration.ofSeconds(30))
//                    .messageConverters(new StringHttpMessageConverter(StandardCharsets.ISO_8859_1))
//                    .build();
        }

//    @Bean public RestTemplate restTemplate() {
//        DefaultUriBuilderFactory defaultUriBuilderFactory = new DefaultUriBuilderFactory();
//        defaultUriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.setUriTemplateHandler(defaultUriBuilderFactory);
//        return restTemplate;
//    }

    @Bean public OpenAPI customOpenAPI() {
        return new OpenAPI().components(new Components()).info(new Info().title("Contact Application API")
                .description("Spring Boot RESTful service using springdoc-openapi and OpenAPI 3."));
    }
}
