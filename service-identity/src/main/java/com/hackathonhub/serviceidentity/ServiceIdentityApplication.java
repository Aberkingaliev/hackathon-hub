package com.hackathonhub.serviceidentity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaRepositories
@ComponentScan(basePackages = {"com.hackathonhub.serviceidentity", "com.hackathonhub.serviceidentity.utils"})
public class ServiceIdentityApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceIdentityApplication.class, args);

	}


}
