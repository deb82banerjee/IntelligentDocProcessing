package com.cts.idp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
public class IntelligentDocProcessingApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntelligentDocProcessingApplication.class, args);
	}
	
	/*
	 * @Bean public RestTemplate getRestTemplate() { return new RestTemplate(); }
	 */
}
