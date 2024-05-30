package com.savemyreceipt.smr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SmrApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmrApplication.class, args);
	}

}
