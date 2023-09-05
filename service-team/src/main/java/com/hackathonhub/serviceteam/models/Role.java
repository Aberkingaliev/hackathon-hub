package com.hackathonhub.serviceteam.models;


import lombok.Data;
import java.util.UUID;


@Data
public class Role {


    private UUID id;

    public UUID getId() {
        return id;
    }

    public RoleEnum getRole_name() {
        return role_name;
    }

    public Role setId(UUID id) {
        this.id = id;
        return this;
    }

    public Role setRole_name(RoleEnum role_name) {
        this.role_name = role_name;
        return this;
    }

    private RoleEnum role_name;
}
