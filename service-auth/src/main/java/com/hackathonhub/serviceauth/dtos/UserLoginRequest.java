package com.hackathonhub.serviceauth.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class UserLoginRequest implements Serializable {

    @JsonCreator
    public UserLoginRequest(@JsonProperty("email") String email,
                            @JsonProperty("password") String password) {
        this.email = email;
        this.password = password;
    }

    private String email;
    private String password;
}
