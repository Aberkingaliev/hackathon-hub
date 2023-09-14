package com.hackathonhub.serviceteam.models;


import lombok.Data;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@Getter
public class TeamMemberId implements Serializable {

    public TeamMemberId userId(UUID userId) {
        this.userId = userId;
        return this;
    }

    public TeamMemberId teamId(UUID teamId) {
        this.teamId = teamId;
        return this;
    }

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "team_id")
    private UUID teamId;

}
