package com.hackathonhub.servicecontest.services;

import com.hackathonhub.servicecontest.constants.ApiSolutionResponseMessage;
import com.hackathonhub.servicecontest.dtos.ApiAuthResponse;
import com.hackathonhub.servicecontest.dtos.solution.SolutionCreateDto;
import com.hackathonhub.servicecontest.dtos.solution.SolutionMetaDto;
import com.hackathonhub.servicecontest.dtos.solution.SolutionUpdateDto;
import com.hackathonhub.servicecontest.models.solution.Solution;
import com.hackathonhub.servicecontest.repositories.SolutionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

@Service
@Slf4j
public class SolutionService {

    @Autowired
    private SolutionRepository solutionRepository;


    public ApiAuthResponse<Solution> createSolution(SolutionCreateDto solution) {
        ApiAuthResponse<Solution> responseBuilder = new ApiAuthResponse<>();
        Solution newSolution = Solution.fromCreateDto(solution);

        try {
            Solution savedSolution = solutionRepository.save(newSolution);

            return responseBuilder.created(savedSolution, ApiSolutionResponseMessage.SOLUTION_CREATED);
        } catch (Exception e) {
            log.error("Error while creating solution: {}", e.getMessage());
            return responseBuilder.internalServerError(e.getMessage());
        }
    }

    public ApiAuthResponse<Solution> getSolutionById(UUID id) {
        ApiAuthResponse<Solution> responseBuilder = new ApiAuthResponse<>();

        try {
            Optional<Solution> foundedSolution = solutionRepository.findById(id);

            return foundedSolution.map(solution ->
                            responseBuilder.ok(solution, ApiSolutionResponseMessage.SOLUTION_FOUND))
                    .orElseGet(() -> responseBuilder.notFound(ApiSolutionResponseMessage.SOLUTION_NOT_FOUND));
        } catch (Exception e) {
            log.error("Error while getting solution: {}", e.getMessage());
            return responseBuilder.internalServerError(e.getMessage());
        }
    }

    public ApiAuthResponse<ArrayList<SolutionMetaDto>> getSolutionMetaByContestId
            (UUID contestId, Integer limit, UUID cursor) {
        ApiAuthResponse<ArrayList<SolutionMetaDto>> responseBuilder = new ApiAuthResponse<>();

        try {
            ArrayList<SolutionMetaDto> foundedSolutionMeta =
                    new ArrayList<>(solutionRepository.getSolutionMetaListById(contestId, limit, cursor));

            return responseBuilder.ok(foundedSolutionMeta, ApiSolutionResponseMessage.SOLUTION_FOUND);
        } catch (Exception e) {
            log.error("Error while getting solution meta list: {}", e.getMessage());
            return responseBuilder.internalServerError(e.getMessage());
        }
    }

    public ApiAuthResponse<Solution> updateSolution(SolutionUpdateDto solution) {
        ApiAuthResponse<Solution> responseBuilder = new ApiAuthResponse<>();

        try {
            Optional<Solution> foundedSolution = solutionRepository.findById(solution.getId());

            return foundedSolution.map(s -> {
                Solution mappedSolution = s.fromUpdateDto(solution);
                Solution updatedSolution = solutionRepository.update(mappedSolution);
                return responseBuilder.ok(updatedSolution, ApiSolutionResponseMessage.SOLUTION_UPDATED);
            }).orElseGet(() -> responseBuilder.notFound(ApiSolutionResponseMessage.SOLUTION_NOT_FOUND));
        } catch (Exception e) {
            log.error("Error while updating solution: {}", e.getMessage());
            return responseBuilder.internalServerError(e.getMessage());
        }
    }

    public ApiAuthResponse<Serializable> deleteSolution(UUID id) {
        ApiAuthResponse<Serializable> responseBuilder = new ApiAuthResponse<>();

        try {
            solutionRepository.deleteById(id);
            return responseBuilder.ok(ApiSolutionResponseMessage.SOLUTION_DELETED);
        } catch (Exception e) {
            log.error("Error while deleting solution: {}", e.getMessage());
            return responseBuilder.internalServerError(e.getMessage());
        }
    }
}
