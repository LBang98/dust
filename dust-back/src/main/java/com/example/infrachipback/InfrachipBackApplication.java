package com.example.infrachipback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@ComponentScan({"com.example.infrachipback.*"})
@Configuration
public class InfrachipBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(InfrachipBackApplication.class, args);
    }

}
