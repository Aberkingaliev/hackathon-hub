package com.hackathonhub.serviceteam.controllers;


import com.hackathonhub.serviceteam.dto.ApiAuthResponse;
import com.hackathonhub.serviceteam.models.TeamMemberId;
import com.hackathonhub.serviceteam.services.TeamMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/member")
public class TeamMemberController {

    @Autowired
    private TeamMemberService teamMemberService;

    @PostMapping
    public ResponseEntity<ApiAuthResponse<String>> addMember(@RequestBody  TeamMemberId newTeamMemberId) {
        ApiAuthResponse<String> response = teamMemberService.addMember(newTeamMemberId);

        return ResponseEntity
                .status(response.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
