package com.hackathonhub.serviceteam.dto;

import com.hackathonhub.serviceteam.models.TeamMember;
import com.hackathonhub.serviceteam.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.UUID;


@Data
@NoArgsConstructor
@Accessors(chain = true)
public class MemberDto implements Serializable {

    private UUID id;
    private String username;
    private String email;

    public MemberDto(UUID id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }
}
