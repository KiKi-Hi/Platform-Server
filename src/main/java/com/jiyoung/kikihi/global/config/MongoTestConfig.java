package com.jiyoung.kikihi.global.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoTestConfig {

    @Bean
    public CommandLineRunner testMongoConnection(MongoTemplate mongoTemplate) {
        return args -> {
            System.out.println("MongoDB connection test: " + mongoTemplate.getDb().getName());
        };
    }
}
