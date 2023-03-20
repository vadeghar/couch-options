package com.couchoptions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CouchOptionsApplication {
	public static void main(String[] args) {
		SpringApplication.run(CouchOptionsApplication.class, args);
	}
}
