package com.hackathonhub.serviceuser.models;


import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    public Role setId(UUID id) {
        this.id = id;
        return this;
    }

    public Role setRole_name(RoleEnum role_name) {
        this.role_name = role_name;
        return this;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private RoleEnum role_name;
}
