package com.hackathonhub.serviceuser.models;


import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Data
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Role implements Serializable {

    @Id
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
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
