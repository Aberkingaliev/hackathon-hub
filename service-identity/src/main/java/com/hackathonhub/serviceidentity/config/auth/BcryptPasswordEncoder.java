package com.hackathonhub.serviceidentity.config.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class BcryptPasswordEncoder implements PasswordEncoder {
    private static final int SALT = 12;
    @Override
    public String encode(CharSequence rawPassword) {
        return BCrypt.hashpw(rawPassword.toString(), BCrypt.gensalt(SALT));
    }

    @Override
    public boolean matches(CharSequence rawPassword, String passwordFromDb) {
        return BCrypt.checkpw(rawPassword.toString(), passwordFromDb);
    }
}
