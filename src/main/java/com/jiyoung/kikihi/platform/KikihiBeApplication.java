package com.jiyoung.kikihi.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableJpaAuditing  // Auditing 활성화
public class KikihiBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(KikihiBeApplication.class, args);
	}

}
