package com.hackathonhub.serviceuser;

import com.hackathonhub.serviceuser.repositories.RoleRepository;
import com.hackathonhub.serviceuser.repositories.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;
import java.util.Arrays;

@SpringBootApplication
@EnableDiscoveryClient
public class ServiceUserApplication {

	public static void main(final String[] args) {
		SpringApplication.run(ServiceUserApplication.class, args);
	}

}
