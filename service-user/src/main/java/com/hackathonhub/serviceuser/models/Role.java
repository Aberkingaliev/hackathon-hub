package com.hackathonhub.serviceuser.models;


import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.util.UUID;

@Entity
@Data
@Table(name = "roles")
public class Role {

    @Id
    private UUID id;

    @PrePersist
    public void generateId() {
        this.id = UUID.randomUUID();
    }

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

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private RoleEnum role_name;
}
