package com.hackathonhub.serviceauth.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserLoginRequest {

    @JsonCreator
    public UserLoginRequest(@JsonProperty("email") String email,
                            @JsonProperty("password") String password) {
        this.email = email;
        this.password = password;
    }

    private String email;
    private String password;
}
