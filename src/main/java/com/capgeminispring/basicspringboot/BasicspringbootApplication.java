package com.capgeminispring.basicspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;

@SpringBootApplication
public class BasicspringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(BasicspringbootApplication.class, args);
	}
	
	@Bean
	public OpenAPI getOpenApI() {
		return new OpenAPI();
	}

}
