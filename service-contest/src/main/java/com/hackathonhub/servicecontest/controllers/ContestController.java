package com.hackathonhub.servicecontest.controllers;

import com.hackathonhub.servicecontest.dtos.ApiAuthResponse;
import com.hackathonhub.servicecontest.dtos.contest.ContestCreateDto;
import com.hackathonhub.servicecontest.dtos.contest.ContestDetailDto;
import com.hackathonhub.servicecontest.models.contest.Contest;
import com.hackathonhub.servicecontest.services.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/contest")
public class ContestController {

    @Autowired
    private ContestService contestService;


    @PostMapping
    public ResponseEntity<ApiAuthResponse<Contest>> createContest(
            @RequestBody ContestCreateDto contest
    ) {
        ApiAuthResponse<Contest> savedContest = contestService.createContest(contest);

        return ResponseEntity
                .status(savedContest.getStatus())
                .body(savedContest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiAuthResponse<ContestDetailDto>> getContestDetailById (
            @PathVariable("id") UUID id
    ) {
        ApiAuthResponse<ContestDetailDto> contest = contestService.getContest(id);

        return ResponseEntity
                .status(contest.getStatus())
                .body(contest);
    }

    @PutMapping
    public ResponseEntity<ApiAuthResponse<Contest>> updateContest(
            @RequestBody Contest contest
    ) {
        ApiAuthResponse<Contest> updatedContest = contestService.updateContest(contest);

        return ResponseEntity
                .status(updatedContest.getStatus())
                .body(updatedContest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiAuthResponse<String>> deleteContest(
            @PathVariable("id") UUID id
    ) {
        ApiAuthResponse<String> deletedContest = contestService.deleteContest(id);

        return ResponseEntity
                .status(deletedContest.getStatus())
                .body(deletedContest);
    }
}
