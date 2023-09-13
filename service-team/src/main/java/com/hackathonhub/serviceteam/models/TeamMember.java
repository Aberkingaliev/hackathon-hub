package com.hackathonhub.serviceteam.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "user_to_team")
public class TeamMember implements Serializable {

    @EmbeddedId
    private TeamMemberId id;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("user_id")
    private User user;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("team_id")
    private Team team;
}
