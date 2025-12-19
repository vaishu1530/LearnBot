package com.example.learnbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class LearnbotApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearnbotApplication.class, args);
    }

    // ADD THIS CODE HERE
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}