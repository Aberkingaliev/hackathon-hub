package com.hackathonhub.serviceidentity.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Data
@Accessors(chain = true)
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


    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private RoleEnum roleName;
}
