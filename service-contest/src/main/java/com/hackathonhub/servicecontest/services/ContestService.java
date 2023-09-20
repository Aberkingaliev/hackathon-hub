package com.hackathonhub.servicecontest.services;

import com.hackathonhub.servicecontest.dtos.ApiAuthResponse;
import com.hackathonhub.servicecontest.dtos.contest.ContestCreateDto;
import com.hackathonhub.servicecontest.dtos.contest.ContestDetailDto;
import com.hackathonhub.servicecontest.dtos.solution.SolutionMetaDto;
import com.hackathonhub.servicecontest.models.contest.Contest;
import com.hackathonhub.servicecontest.repositories.ContestRepository;
import com.hackathonhub.servicecontest.repositories.SolutionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class ContestService {

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private SolutionRepository solutionRepository;


    public ApiAuthResponse<Contest> createContest(ContestCreateDto contest) {
        Contest newContest = Contest.fromCreateDto(contest);

        try {
            Contest savedContest = contestRepository.save(newContest);

            return ApiAuthResponse.<Contest>builder()
                    .status(HttpStatus.CREATED)
                    .data(savedContest)
                    .message("CONTEST_CREATED")
                    .build();
        } catch (Exception e) {
            log.error("Error while creating contest: {}", e.getMessage());
            return ApiAuthResponse.<Contest>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("ERROR_WHILE_CREATING_CONTEST")
                    .build();
        }
    }

    public ApiAuthResponse<ContestDetailDto> getContest(UUID contestId) {
        ApiAuthResponse.ApiAuthResponseBuilder<ContestDetailDto> responseBuilder =
                ApiAuthResponse.<ContestDetailDto>builder();

        try {
            Optional<ContestDetailDto> foundedContest =
                    contestRepository.getContestDetailById(contestId);
            Set<SolutionMetaDto> foundedSolutions =
                    solutionRepository.getSolutionsMetaByContestId(contestId,10,null);
            foundedContest
                    .ifPresent(contestDto -> contestDto.setSolutions(foundedSolutions));


            return responseBuilder
                    .status(HttpStatus.OK)
                    .data(foundedContest.orElse(null))
                    .message("CONTEST_FOUND")
                    .build();
        } catch (Exception e) {
            log.error("Error while getting contest: {}", e.getMessage());

            return responseBuilder
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("ERROR_WHILE_GETTING_CONTEST")
                    .build();
        }
    }

    public ApiAuthResponse<Contest> updateContest(Contest contest) {
        ApiAuthResponse.ApiAuthResponseBuilder<Contest> responseBuilder =
                ApiAuthResponse.<Contest>builder();

        try {
            Contest updatedContest = contestRepository.updateContest(contest);

            return responseBuilder
                    .status(HttpStatus.OK)
                    .data(updatedContest)
                    .message("CONTEST_UPDATED")
                    .build();
        } catch (Exception e) {
            log.error("Error while updating contest: {}", e.getMessage());

            return responseBuilder
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("ERROR_WHILE_UPDATING_CONTEST")
                    .build();
        }
    }

    public ApiAuthResponse<String> deleteContest(UUID id) {
        ApiAuthResponse.ApiAuthResponseBuilder<String> responseBuilder =
                ApiAuthResponse.<String>builder();

        try {
            contestRepository.deleteContestById(id);

            return responseBuilder
                    .status(HttpStatus.OK)
                    .data("CONTEST_DELETED")
                    .message("CONTEST_DELETED")
                    .build();
        } catch (Exception e) {
            log.error("Error while deleting contest: {}", e.getMessage());

            return responseBuilder
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("ERROR_WHILE_DELETING_CONTEST")
                    .build();
        }
    }
}
