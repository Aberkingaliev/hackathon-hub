package com.hackathonhub.serviceidentity.config;

import com.hackathonhub.serviceidentity.models.AuthTokenRedis;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;


    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfig = new RedisStandaloneConfiguration();
        redisStandaloneConfig.setHostName(redisHost);
        redisStandaloneConfig.setPort(redisPort);

        return new LettuceConnectionFactory(redisStandaloneConfig);
    }

    @Bean
    RedisOperations<String, AuthTokenRedis> redisOperations
            (LettuceConnectionFactory connFactory) {
        RedisTemplate<String, AuthTokenRedis> template = new RedisTemplate<>();
        template.setConnectionFactory(connFactory);

        return template;
    }
}