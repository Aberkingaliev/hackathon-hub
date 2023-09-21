package com.hackathonhub.servicecontest.controllers;


import com.hackathonhub.servicecontest.dtos.ApiAuthResponse;
import com.hackathonhub.servicecontest.dtos.solution.SolutionCreateDto;
import com.hackathonhub.servicecontest.models.solution.Solution;
import com.hackathonhub.servicecontest.services.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/solution")
public class SolutionController {

    @Autowired
    private SolutionService solutionService;

    @PostMapping
    public ResponseEntity<ApiAuthResponse<Solution>> createSolution(
            @RequestBody SolutionCreateDto solution
    ) {
        ApiAuthResponse<Solution> savedSolution = solutionService.createSolution(solution);

        return ResponseEntity
                .status(savedSolution.getStatus())
                .body(savedSolution);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiAuthResponse<Solution>> getSolutionById(
            @PathVariable("id") UUID id
    ) {
        ApiAuthResponse<Solution> solution = solutionService.getSolutionById(id);

        return ResponseEntity
                .status(solution.getStatus())
                .body(solution);
    }

    @PutMapping
    public ResponseEntity<ApiAuthResponse<Solution>> updateSolution(
            @RequestBody Solution solution
    ) {
        ApiAuthResponse<Solution> updatedSolution = solutionService.updateSolution(solution);

        return ResponseEntity
                .status(updatedSolution.getStatus())
                .body(updatedSolution);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiAuthResponse<String>> deleteSolutionById(
            @PathVariable("id") UUID id
    ) {
        ApiAuthResponse<String> deletedSolution = solutionService.deleteSolution(id);

        return ResponseEntity
                .status(deletedSolution.getStatus())
                .body(deletedSolution);
    }

}
