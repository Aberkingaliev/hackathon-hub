package com.hackathonhub.serviceteam.controllers;


import com.hackathonhub.serviceteam.dto.ApiAuthResponse;
import com.hackathonhub.serviceteam.models.Team;
import com.hackathonhub.serviceteam.services.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @Operation(summary = "Create team", description = "If the response is successful, the created team will be " +
            "returned in the response body (data: Team)")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "201", description = "Team created"),
                    @ApiResponse(responseCode = "400", description = "Bad Request",
                            content = @Content(schema = @Schema(implementation = ApiAuthResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(schema = @Schema(implementation = ApiAuthResponse.class)))
            }
    )
    @PostMapping("/team/create")
    public ResponseEntity<ApiAuthResponse<Team>> createTeam(@RequestBody Team team) {
        ApiAuthResponse<Team> response = teamService.createTeam(team);
        return ResponseEntity
                .status(response.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Operation(summary = "Get team by id", description = "If the response is successful, the found team will be " +
            "returned in the body of the response (data: Team)")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "200", description = "Team for this id has been found"),
                    @ApiResponse(responseCode = "400", description = "Bad Request",
                            content = @Content(schema = @Schema(implementation = ApiAuthResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Team for this id was not found",
                            content = @Content(schema = @Schema(implementation = ApiAuthResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(schema = @Schema(implementation = ApiAuthResponse.class)))
            }
    )
    @GetMapping("/team/{id}")
    public ResponseEntity<ApiAuthResponse<Team>> getTeamById(@PathVariable("id") String id) {
        ApiAuthResponse<Team> response = teamService.getTeamById(id);
        return ResponseEntity
                .status(response.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Operation(summary = "Delete team by id", description = "If the response is successful, the deleted team will be " +
            "returned in the response body (data: Team)")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "200", description = "Team deleted"),
                    @ApiResponse(responseCode = "400", description = "Bad Request",
                            content = @Content(schema = @Schema(implementation = ApiAuthResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Team for this id was not found",
                            content = @Content(schema = @Schema(implementation = ApiAuthResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(schema = @Schema(implementation = ApiAuthResponse.class)))
            }
    )
    @DeleteMapping("/team/{id}")
    public ResponseEntity<ApiAuthResponse<Team>> deleteTeamById(@PathVariable("id") String id) {
        ApiAuthResponse<Team> response = teamService.deleteTeamById(id);
        return ResponseEntity
                .status(response.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
