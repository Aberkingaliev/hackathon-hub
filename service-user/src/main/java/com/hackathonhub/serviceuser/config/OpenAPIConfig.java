package com.hackathonhub.serviceuser.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@OpenAPIDefinition
@Configuration
public class OpenAPIConfig {

    @Value("${openapi.service.url}")
    private String URL;
    @Value("${openapi.service.title}")
    private String TITLE;
    @Value("${openapi.service.version}")
    private String VERSION;


    @Bean
    public OpenAPI customizeOpenApi() {
        return new OpenAPI()
                .components(new Components())
                .servers(List.of(new Server().url(URL)))
                .info(new Info()
                        .title(TITLE)
                        .contact(
                                new Contact()
                                        .email("erkingaliev21@gmail.com")
                                        .url("https://github.com/Aberkingaliev")
                        )
                        .version(VERSION)
                );
    }
}
