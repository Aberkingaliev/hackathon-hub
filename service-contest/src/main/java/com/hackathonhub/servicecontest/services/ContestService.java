package com.hackathonhub.servicecontest.services;

import com.hackathonhub.servicecontest.constants.ApiContestResponseMessage;
import com.hackathonhub.servicecontest.dtos.ApiAuthResponse;
import com.hackathonhub.servicecontest.dtos.contest.ContestCreateDto;
import com.hackathonhub.servicecontest.dtos.contest.ContestDetailDto;
import com.hackathonhub.servicecontest.dtos.contest.ContestUpdateDto;
import com.hackathonhub.servicecontest.dtos.solution.SolutionMetaDto;
import com.hackathonhub.servicecontest.models.contest.Contest;
import com.hackathonhub.servicecontest.repositories.ContestRepository;
import com.hackathonhub.servicecontest.repositories.SolutionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
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
        ApiAuthResponse<Contest> responseBuilder = new ApiAuthResponse<>();
        Contest newContest = Contest.fromCreateDto(contest);

        try {
            Contest savedContest = contestRepository.save(newContest);

            return responseBuilder.created(savedContest, ApiContestResponseMessage.CONTEST_CREATED);
        } catch (Exception e) {
            log.error("Error while creating contest: {}", e.getMessage());
            return responseBuilder.internalServerError(e.getMessage());
        }
    }

    public ApiAuthResponse<ContestDetailDto> getContest(UUID contestId) {
        ApiAuthResponse<ContestDetailDto> responseBuilder = new ApiAuthResponse<>();

        try {
            Optional<ContestDetailDto> foundedContest =
                    contestRepository.getDetailById(contestId);

            return foundedContest.map(contest -> {
                Set<SolutionMetaDto> foundedSolutions =
                        solutionRepository.getSolutionMetaListById(contestId, 10, null);
                contest.setSolutions(foundedSolutions);
                return responseBuilder.ok(contest, ApiContestResponseMessage.CONTEST_FOUND);
            }).orElseGet(() -> responseBuilder.notFound(ApiContestResponseMessage.CONTEST_NOT_FOUND));
        } catch (Exception e) {
            log.error("Error while getting contest: {}", e.getMessage());
            return responseBuilder.internalServerError(e.getMessage());
        }
    }

    public ApiAuthResponse<Contest> updateContest(ContestUpdateDto contest) {
        ApiAuthResponse<Contest> responseBuilder = new ApiAuthResponse<>();

        try {
            Optional<Contest> foundedContest = contestRepository.findById(contest.getId());

            return foundedContest.map(c -> {
                Contest mappedContest = c.fromUpdateDto(contest);
                Contest updatedContest = contestRepository.update(mappedContest);
                return responseBuilder.ok(updatedContest, ApiContestResponseMessage.CONTEST_UPDATED);
            }).orElseGet(() -> responseBuilder.notFound(ApiContestResponseMessage.CONTEST_NOT_FOUND));
        } catch (Exception e) {
            log.error("Error while updating contest: {}", e.getMessage());
            return responseBuilder.internalServerError(e.getMessage());
        }
    }

    public ApiAuthResponse<Serializable> deleteContest(UUID id) {
        ApiAuthResponse<Serializable> responseBuilder = new ApiAuthResponse<>();

        try {
            contestRepository.deleteById(id);

            return responseBuilder.ok(ApiContestResponseMessage.CONTEST_DELETED);
        } catch (Exception e) {
            log.error("Error while deleting contest: {}", e.getMessage());
            return responseBuilder.internalServerError(e.getMessage());
        }
    }
}
