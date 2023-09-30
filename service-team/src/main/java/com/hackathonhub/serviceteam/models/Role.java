package com.hackathonhub.serviceteam.models;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Data
@Table(name = "roles")
public class Role implements Serializable {

    @Id
    private UUID id;

    @PrePersist
    public void generateId() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public RoleEnum getRoleName() {
        return roleName;
    }

    public Role setId(UUID id) {
        this.id = id;
        return this;
    }

    public Role setRoleName(RoleEnum roleName) {
        this.roleName = roleName;
        return this;
    }

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private RoleEnum roleName;
}
