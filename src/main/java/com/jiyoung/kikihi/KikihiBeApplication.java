package com.jiyoung.kikihi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing  // Auditing 활성화
public class KikihiBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(KikihiBeApplication.class, args);
	}

}
