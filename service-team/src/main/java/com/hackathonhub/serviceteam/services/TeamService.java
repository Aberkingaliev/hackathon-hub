package com.hackathonhub.serviceteam.services;

import com.hackathonhub.serviceteam.dto.ApiAuthResponse;
import com.hackathonhub.serviceteam.dto.TeamCreateDto;
import com.hackathonhub.serviceteam.dto.TeamDto;
import com.hackathonhub.serviceteam.models.Team;
import com.hackathonhub.serviceteam.repositories.TeamRepository;
import com.hackathonhub.user_protos.grpc.UserServiceGrpc;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;


@Service
@Slf4j
public class TeamService {

    @GrpcClient("service-user")
    private UserServiceGrpc.UserServiceBlockingStub userStub;

    @Autowired
    private TeamRepository teamRepository;

    public ApiAuthResponse<TeamDto> createTeam(TeamCreateDto team) {
        ApiAuthResponse<TeamDto> responseBuilder = new ApiAuthResponse<>();
        Team mappedTeam = Team.fromCreateDto(team);

        try {
            Team savedTeam = teamRepository.save(mappedTeam);
            return responseBuilder.created(savedTeam.toDto(), "Team created successfully");
        } catch (Exception e) {
            log.error("Error creating team: {}", e.getMessage());
            return responseBuilder.internalServerError(e.getMessage());
        }
    }

    public ApiAuthResponse<TeamDto> getTeamById(String id) {
        UUID parsedId = UUID.fromString(id);
        ApiAuthResponse<TeamDto> responseBuilder = new ApiAuthResponse<>();

        try {
            Optional<Team> team = teamRepository.findById(parsedId);
            return team.map(t -> responseBuilder.ok(t.toDto(), "Team retrieved successfully"))
                    .orElseGet(() -> responseBuilder.notFound("Team not found"));
        } catch (Exception e) {
            log.error("Error retrieving team: {}", e.getMessage());
            return responseBuilder.internalServerError(e.getMessage());
        }
    }

    public ApiAuthResponse deleteTeamById(String id) {
        UUID parsedId = UUID.fromString(id);
        ApiAuthResponse<Serializable> responseBuilder = new ApiAuthResponse<>();

        try {
            teamRepository.deleteById(parsedId);
            return responseBuilder.ok("Team deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting team: {}", e.getMessage());
            return responseBuilder.internalServerError(e.getMessage());
        }
    }
}
