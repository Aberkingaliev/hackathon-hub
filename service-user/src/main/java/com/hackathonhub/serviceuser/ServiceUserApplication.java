package com.hackathonhub.serviceuser;

import io.grpc.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaRepositories
public class ServiceUserApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ServiceUserApplication.class);
		ConfigurableApplicationContext context = app.run(args);

		Server grpcServer =  context.getBean(Server.class);

		try {
			grpcServer.start();

			grpcServer.awaitTermination();
		} catch (IOException | InterruptedException e) {
			System.out.println(e.getMessage());
			grpcServer.shutdown();
		}
	}

}
