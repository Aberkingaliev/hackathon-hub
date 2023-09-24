package com.hackathonhub.serviceteam.services;

import com.hackathonhub.serviceteam.constants.ApiTeamMemberResponseMessage;
import com.hackathonhub.serviceteam.dto.ApiAuthResponse;
import com.hackathonhub.serviceteam.dto.MemberDto;
import com.hackathonhub.serviceteam.models.TeamMember;
import com.hackathonhub.serviceteam.models.TeamMemberId;
import com.hackathonhub.serviceteam.repositories.TeamMemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        ApiAuthResponse<String> responseBuilder = new ApiAuthResponse<>();
        TeamMemberId newTeamMemberId = new TeamMemberId(userId, teamId);

        if (userId == null || teamId == null) {
            return responseBuilder.badRequest(ApiTeamMemberResponseMessage.INVALID_ID);
        }

        try {
            Optional<TeamMember> foundedTeamMember = teamMemberRepository.findById(newTeamMemberId);

            if (foundedTeamMember.isPresent()) {
                return responseBuilder.conflict(ApiTeamMemberResponseMessage.USER_ALREADY_IN_TEAM);
            }

            TeamMember newTeamMember = new TeamMember().setEmbeddedId(newTeamMemberId);
            teamMemberRepository.save(newTeamMember);
            return responseBuilder.created(ApiTeamMemberResponseMessage.USER_ADDED_TO_TEAM);
        } catch (Exception e) {
            log.error("Error creating team member: {}", e.getMessage());
            return responseBuilder.internalServerError(e.getMessage());
        }
    }

    public ApiAuthResponse<HashSet<MemberDto>> getMembers(UUID teamId, UUID cursor, int limit) {
        ApiAuthResponse<HashSet<MemberDto>> responseBuilder = new ApiAuthResponse<>();

        if (teamId == null) {
            return responseBuilder.badRequest(ApiTeamMemberResponseMessage.INVALID_ID);
        }

        try {
            Pageable pageRequest = PageRequest.of(0, limit);
            List<MemberDto> allMembers = teamMemberRepository
                    .findMembersByTeamId(teamId, cursor, pageRequest);

            return responseBuilder.ok(new HashSet<>(allMembers),
                    ApiTeamMemberResponseMessage.MEMBERS_RECEIVED);
        } catch (Exception e) {
            log.error("Error retrieving team members: {}", e.getMessage());
            return responseBuilder.internalServerError(e.getMessage());
        }
    }

    public ApiAuthResponse<String> deleteMember(UUID teamId, UUID userId) {
        ApiAuthResponse<String> responseBuilder = new ApiAuthResponse<>();

        if (teamId == null || userId == null) {
            return responseBuilder.badRequest(ApiTeamMemberResponseMessage.INVALID_ID);
        }

        TeamMemberId teamMemberId = new TeamMemberId(userId, teamId);
        try {
            Optional<TeamMember> foundedTeamMember = teamMemberRepository.findById(teamMemberId);

            if (foundedTeamMember.isEmpty()) {
                return responseBuilder.notFound(ApiTeamMemberResponseMessage.USER_NOT_FOUND_IN_TEAM);
            }

            teamMemberRepository.deleteById(teamMemberId);
            return responseBuilder.ok(ApiTeamMemberResponseMessage.USER_DELETED_FROM_TEAM);
        } catch (Exception e) {
            log.error("Error deleting team member: {}", e.getMessage());
            return responseBuilder.internalServerError(e.getMessage());
        }
    }
}
