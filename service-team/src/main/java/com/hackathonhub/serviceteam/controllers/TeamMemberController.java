package com.hackathonhub.serviceteam.controllers;


import com.hackathonhub.serviceteam.dto.ApiAuthResponse;
import com.hackathonhub.serviceteam.dto.MemberDto;
import com.hackathonhub.serviceteam.services.TeamMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api")
public class TeamMemberController {

    @Autowired
    private TeamMemberService teamMemberService;

    @PostMapping("/team/{id}/member")
    public ResponseEntity<ApiAuthResponse<String>> addMember(
            @RequestBody  Map<String, UUID> userIdObject,
            @PathVariable("id") UUID teamId
    ) {
        ApiAuthResponse<String> response = teamMemberService
                .addMember(teamId, userIdObject.get("userId"));

        return ResponseEntity
                .status(response.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/team/{id}/member")
    public ResponseEntity<ApiAuthResponse<HashSet<MemberDto>>> getAllMembers(
            @PathVariable("id") UUID teamId,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "cursor", required = false) UUID cursor
    ) {
        ApiAuthResponse<HashSet<MemberDto>> response = teamMemberService.getMembers(teamId, cursor, limit);

        return ResponseEntity
                .status(response.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/team/{id}/member")
    public ResponseEntity<ApiAuthResponse<String>> deleteMember(
            @RequestBody Map<String, UUID> userIdObject,
            @PathVariable("id") UUID teamId
    ) {
        ApiAuthResponse<String> response = teamMemberService
                .deleteMember(teamId, userIdObject.get("userId"));

        return ResponseEntity
                .status(response.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
