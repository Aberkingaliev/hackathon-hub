package com.hackathonhub.serviceauth.dtos;

import com.hackathonhub.serviceauth.grpc.UserGrpcService;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class UserPayload {

    private UUID id;

    public UserPayload setId(UUID id) {
        this.id = id;
        return this;
    }

    public UserPayload setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserPayload setIsActivate(Boolean activate) {
        this.isActivate = activate;
        return this;
    }

    public UserPayload setRole(UserGrpcService.role_enum role) {
        this.role = role;
        return this;
    }

    private String email;
    private Boolean isActivate;
    private UserGrpcService.role_enum role;

    public HashMap<String, String> toHashMap() {
        return new HashMap<>(
                Map.ofEntries(
                        Map.entry("id", id.toString()),
                        Map.entry("email", email),
                        Map.entry("isActivated", isActivate.toString()),
                        Map.entry("role", role.toString())
                )
        );
    }


}
