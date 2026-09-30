package com.creditcard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.Bean;
@SpringBootApplication
@ComponentScan(basePackages = "com.creditcard")
public class CardServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardServiceApplication.class, args);
        System.out.println("Clinet Working ");
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}