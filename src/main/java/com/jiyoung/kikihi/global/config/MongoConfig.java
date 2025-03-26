package com.jiyoung.kikihi.global.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.example.repository")
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "kikihi";  // 🔹 접근할 MongoDB 데이터베이스 이름
    }

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb://localhost:37017");  // 🔹 MongoDB URL
    }
}
