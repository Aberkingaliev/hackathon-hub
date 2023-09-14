package com.hackathonhub.serviceteam.services;

import com.hackathonhub.serviceteam.constants.ApiTeamMemberResponseMessage;
import com.hackathonhub.serviceteam.dto.ApiAuthResponse;
import com.hackathonhub.serviceteam.models.TeamMember;
import com.hackathonhub.serviceteam.models.TeamMemberId;
import com.hackathonhub.serviceteam.repositories.TeamMemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class TeamMemberService {

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    public ApiAuthResponse<String> addMember(TeamMemberId newTeamMemberId) {
        ApiAuthResponse.ApiAuthResponseBuilder<String> responseBuilder = ApiAuthResponse.builder();
        TeamMember newTeamMember = new TeamMember();
        newTeamMember.setId(newTeamMemberId);

        try {
            teamMemberRepository.save(newTeamMember);

            return responseBuilder
                    .status(HttpStatus.OK)
                    .message(ApiTeamMemberResponseMessage.USER_ADDED_TO_TEAM)
                    .build();
        } catch (Exception e) {
            log.error("Error creating team member: {}", e.getMessage());

            return responseBuilder
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(e.getMessage())
                    .build();
        }
    }

}
