package com.hackathonhub.serviceteam;


import com.hackathonhub.serviceteam.dto.ApiAuthResponse;
import com.hackathonhub.serviceteam.models.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@RestController
@RequestMapping("/api")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @PostMapping("/team/create")
    public ResponseEntity<ApiAuthResponse<Team>> createTeam(@RequestBody Team team) {
        ApiAuthResponse<Team> response = teamService.createTeam(team);
        return ResponseEntity
                .status(response.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/team/{id}")
    public ResponseEntity<ApiAuthResponse<Team>> getTeamById(@PathVariable("id") String id) {
        ApiAuthResponse<Team> response = teamService.getTeamById(id);
        return ResponseEntity
                .status(response.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/team/{id}")
    public ResponseEntity<ApiAuthResponse<Team>> deleteTeamById(@PathVariable("id") String id) {
        ApiAuthResponse<Team> response = teamService.deleteTeamById(id);
        return ResponseEntity
                .status(response.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
