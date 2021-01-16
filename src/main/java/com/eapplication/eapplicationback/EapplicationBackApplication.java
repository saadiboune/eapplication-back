package com.eapplication.eapplicationback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
@ComponentScan(basePackages = "com.eapplication.eapplicationback")
public class EapplicationBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(EapplicationBackApplication.class, args);
    }

}
