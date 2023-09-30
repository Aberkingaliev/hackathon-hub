package com.hackathonhub.serviceteam.models;

import com.hackathonhub.serviceteam.dto.TeamCreateDto;
import com.hackathonhub.serviceteam.dto.TeamDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

@Entity
@Getter
@ToString
@Table(name = "teams", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
public class Team implements Serializable {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    private UUID id;

    @PrePersist
    public void prePersistValues() {
        this.id = UUID.randomUUID();
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    @Column(name = "name")
    private String name;

    public Team setName(String name) {
        this.name = name;
        return this;
    }

    @Column(name = "description")
    private String description;

    public Team setDescription(String description) {
        this.description = description;
        return this;
    }

    @Column(name = "founder_id")
    private UUID founderId;

    public Team setFounderId(UUID founderId) {
        this.founderId = founderId;
        return this;
    }

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "created_at")
    private Timestamp createdAt;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_to_team",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> members;

    public static Team fromCreateDto(TeamCreateDto dto) {
        return new Team()
                .setName(dto.getName())
                .setDescription(dto.getDescription())
                .setFounderId(dto.getFounderId());
    }

    public Team fromDto(TeamDto dto) {
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.founderId = dto.getFounderId();
        return this;
    }

    public TeamDto toDto() {
        return TeamDto.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .founderId(this.founderId)
                .createdAt(this.createdAt)
                .build();
    }
}