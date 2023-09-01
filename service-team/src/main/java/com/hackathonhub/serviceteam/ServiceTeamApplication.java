package com.hackathonhub.serviceteam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ServiceTeamApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceTeamApplication.class, args);
	}

}
