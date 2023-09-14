package com.hackathonhub.serviceteam.services;

import com.hackathonhub.serviceteam.constants.ApiTeamMemberResponseMessage;
import com.hackathonhub.serviceteam.dto.ApiAuthResponse;
import com.hackathonhub.serviceteam.models.TeamMember;
import com.hackathonhub.serviceteam.models.TeamMemberId;
import com.hackathonhub.serviceteam.models.User;
import com.hackathonhub.serviceteam.repositories.TeamMemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Slf4j
public class TeamMemberService {

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    public ApiAuthResponse<String> addMember(UUID teamId, UUID userId) {
        ApiAuthResponse.ApiAuthResponseBuilder<String> responseBuilder = ApiAuthResponse.builder();
        TeamMemberId newTeamMemberId = new TeamMemberId()
                .teamId(teamId)
                .userId(userId);
        TeamMember newTeamMember = new TeamMember()
                .id(newTeamMemberId);

        try {
            Optional<TeamMember> foundedTeamMember = teamMemberRepository.findById(newTeamMemberId);

            if (foundedTeamMember.isPresent()) {
                return responseBuilder
                        .status(HttpStatus.BAD_REQUEST)
                        .message(ApiTeamMemberResponseMessage.USER_ALREADY_IN_TEAM)
                        .build();
            }

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

    public ApiAuthResponse<HashSet<User>> getMembers(UUID teamId, UUID cursor, int limit) {
        ApiAuthResponse.ApiAuthResponseBuilder<HashSet<User>> responseBuilder =
                ApiAuthResponse.builder();
        try {
            Pageable pageRequest = PageRequest.of(0, limit);
            List<User> allMembers = teamMemberRepository
                    .findMembersByTeamId(teamId, cursor, pageRequest);

            return responseBuilder
                    .status(HttpStatus.OK)
                    .message(ApiTeamMemberResponseMessage.MEMBERS_RECEIVED)
                    .data(new HashSet<>(allMembers))
                    .build();
        } catch (Exception e) {
            log.error("Error retrieving team members: {}", e.getMessage());

            return responseBuilder
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(e.getMessage())
                    .build();
        }
    }
}
