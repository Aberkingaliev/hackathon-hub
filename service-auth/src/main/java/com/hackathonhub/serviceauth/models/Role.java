package com.hackathonhub.serviceauth.models;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class Role implements Serializable {

    private UUID id;

    public Role setId(UUID id) {
        this.id = id;
        return this;
    }

    public Role setRoleName(RoleEnum roleName) {
        this.roleName = roleName;
        return this;
    }

    private RoleEnum roleName;
}
