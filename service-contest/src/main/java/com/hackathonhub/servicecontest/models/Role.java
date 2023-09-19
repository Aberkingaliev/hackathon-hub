package com.hackathonhub.servicecontest.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
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

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private RoleEnum role_name;
}
