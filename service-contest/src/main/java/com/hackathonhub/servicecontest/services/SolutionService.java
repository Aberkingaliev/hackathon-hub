package com.hackathonhub.servicecontest.services;

import com.hackathonhub.servicecontest.constants.ApiSolutionResponseMessage;
import com.hackathonhub.servicecontest.dtos.ApiAuthResponse;
import com.hackathonhub.servicecontest.dtos.solution.SolutionCreateDto;
import com.hackathonhub.servicecontest.dtos.solution.SolutionMetaDto;
import com.hackathonhub.servicecontest.models.solution.Solution;
import com.hackathonhub.servicecontest.repositories.SolutionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class SolutionService {

    @Autowired
    private SolutionRepository solutionRepository;


    public ApiAuthResponse<Solution> createSolution(SolutionCreateDto solution) {
        Solution newSolution = Solution.fromCreateDto(solution);

        try {
            Solution savedSolution = solutionRepository.save(newSolution);

            return ApiAuthResponse.<Solution>builder()
                    .status(HttpStatus.CREATED)
                    .data(savedSolution)
                    .message(ApiSolutionResponseMessage.SOLUTION_CREATED)
                    .build();
        } catch (Exception e) {
            log.error("Error while creating solution: {}", e.getMessage());
            return ApiAuthResponse.<Solution>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(e.getMessage())
                    .build();
        }
    }

    public ApiAuthResponse<Solution> getSolutionById(UUID id) {
        ApiAuthResponse.ApiAuthResponseBuilder<Solution> responseBuilder =
                ApiAuthResponse.<Solution>builder();

        try {
            Optional<Solution> foundedSolution = solutionRepository.findById(id);

            return foundedSolution.map(solution ->
                            responseBuilder
                                    .status(HttpStatus.OK)
                                    .data(solution)
                                    .message(ApiSolutionResponseMessage.SOLUTION_FOUND)
                                    .build())
                    .orElseGet(() ->
                            responseBuilder
                                    .status(HttpStatus.NOT_FOUND)
                                    .message(ApiSolutionResponseMessage.SOLUTION_NOT_FOUND)
                                    .build());
        } catch (Exception e) {
            log.error("Error while getting solution: {}", e.getMessage());
            return ApiAuthResponse.<Solution>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(e.getMessage())
                    .build();
        }
    }

    public ApiAuthResponse<ArrayList<SolutionMetaDto>> getSolutionMetaByContestId
            (UUID contestId, Integer limit, UUID cursor) {
        ApiAuthResponse.ApiAuthResponseBuilder<ArrayList<SolutionMetaDto>> responseBuilder =
                ApiAuthResponse.<ArrayList<SolutionMetaDto>>builder();

        try {
            ArrayList<SolutionMetaDto> foundedSolutionMeta =
                    new ArrayList<>(solutionRepository.getSolutionMetaListById(contestId, limit, cursor));

            return responseBuilder
                    .status(HttpStatus.OK)
                    .data(foundedSolutionMeta)
                    .message(ApiSolutionResponseMessage.SOLUTION_FOUND)
                    .build();
        } catch (Exception e) {
            log.error("Error while getting solution meta list: {}", e.getMessage());
            return ApiAuthResponse.<ArrayList<SolutionMetaDto>>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(e.getMessage())
                    .build();
        }
    }

    public ApiAuthResponse<Solution> updateSolution(Solution solution) {
        ApiAuthResponse.ApiAuthResponseBuilder<Solution> responseBuilder =
                ApiAuthResponse.<Solution>builder();

        try {
            Optional<Solution> foundedSolution = solutionRepository.findById(solution.getId());

            return foundedSolution.map(s -> {
                Solution updatedSolution = solutionRepository.update(s);
                return responseBuilder
                        .status(HttpStatus.OK)
                        .data(updatedSolution)
                        .message(ApiSolutionResponseMessage.SOLUTION_UPDATED)
                        .build();
            }).orElseGet(() -> responseBuilder
                    .status(HttpStatus.NOT_FOUND)
                    .message(ApiSolutionResponseMessage.SOLUTION_NOT_FOUND)
                    .build());

        } catch (Exception e) {
            log.error("Error while updating solution: {}", e.getMessage());
            return ApiAuthResponse.<Solution>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(e.getMessage())
                    .build();
        }
    }

    public ApiAuthResponse<String> deleteSolution(UUID id) {
        ApiAuthResponse.ApiAuthResponseBuilder<String> responseBuilder =
                ApiAuthResponse.builder();

        try {
            solutionRepository.deleteById(id);

            return responseBuilder
                    .status(HttpStatus.OK)
                    .message(ApiSolutionResponseMessage.SOLUTION_DELETED)
                    .build();
        } catch (Exception e) {
            log.error("Error while deleting solution: {}", e.getMessage());
            return ApiAuthResponse.<String>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(e.getMessage())
                    .build();
        }
    }
}
