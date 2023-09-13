package com.hackathonhub.serviceteam.services;

import com.hackathonhub.serviceteam.dto.ApiAuthResponse;
import com.hackathonhub.serviceteam.models.Team;
import com.hackathonhub.serviceteam.repositories.TeamRepository;
import com.hackathonhub.user_protos.grpc.UserServiceGrpc;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Service
@Slf4j
public class TeamService {

    @GrpcClient("service-user")
    private UserServiceGrpc.UserServiceBlockingStub userStub;

    @Autowired
    private TeamRepository teamRepository;

    public ApiAuthResponse<Team> createTeam(Team team) {
        ApiAuthResponse.ApiAuthResponseBuilder<Team> responseBuilder = ApiAuthResponse.builder();

        try {
            Team savedTeam = teamRepository.save(team);
            return responseBuilder
                    .status(HttpStatus.CREATED)
                    .message("Team created successfully")
                    .data(savedTeam)
                    .build();
        } catch (Exception e) {
            log.error("Error creating team: {}", e.getMessage());
            return responseBuilder
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(e.getMessage())
                    .build();
        }
    }

    public ApiAuthResponse<Team> getTeamById (String id) {
        UUID parsedId = UUID.fromString(id);
        ApiAuthResponse.ApiAuthResponseBuilder<Team> responseBuilder = ApiAuthResponse.builder();

        try {
            Optional<Team> team = teamRepository.findById(parsedId);

            if(team.isEmpty()) {
                return responseBuilder
                        .status(HttpStatus.NOT_FOUND)
                        .message("Team not found")
                        .build();
            }

            return responseBuilder
                    .status(HttpStatus.OK)
                    .message("Team retrieved successfully")
                    .data(team.get())
                    .build();
        } catch (Exception e) {
            log.error("Error retrieving team: {}", e.getMessage());
            return responseBuilder
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(e.getMessage())
                    .build();
        }
    }

    public ApiAuthResponse<Team> deleteTeamById (String id) {
        UUID parsedId = UUID.fromString(id);
        ApiAuthResponse.ApiAuthResponseBuilder<Team> responseBuilder = ApiAuthResponse.builder();

        try {
            teamRepository.deleteById(parsedId);
            return responseBuilder
                    .status(HttpStatus.OK)
                    .message("Team deleted successfully")
                    .build();
        } catch (Exception e) {
            log.error("Error deleting team: {}", e.getMessage());
            return responseBuilder
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(e.getMessage())
                    .build();
        }
    }
}
