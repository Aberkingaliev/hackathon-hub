package com.hackathonhub.servicecontest;


import com.hackathonhub.servicecontest.dtos.ApiAuthResponse;
import com.hackathonhub.servicecontest.dtos.contest.ContestCreateDto;
import com.hackathonhub.servicecontest.dtos.contest.ContestDetailDto;
import com.hackathonhub.servicecontest.dtos.solution.SolutionMetaDto;
import com.hackathonhub.servicecontest.models.User;
import com.hackathonhub.servicecontest.models.contest.Contest;
import com.hackathonhub.servicecontest.models.contest.ContestCategory;
import com.hackathonhub.servicecontest.models.contest.ContestStatus;
import com.hackathonhub.servicecontest.repositories.ContestRepository;
import com.hackathonhub.servicecontest.repositories.SolutionRepository;
import com.hackathonhub.servicecontest.services.ContestService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith({MockitoExtension.class, SpringExtension.class})
public class ContestServiceTest {

    @Mock
    private ContestRepository contestRepository;

    @Mock
    private SolutionRepository solutionRepository;

    @InjectMocks
    @Autowired
    private ContestService contestService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createContest_TestValid() throws Exception {
        ContestCreateDto contestCreateDto = new ContestCreateDto(
                UUID.randomUUID(),
                "name",
                "description",
                new HashSet<>(),
                new Date()
        );
        Contest contest = Contest.fromCreateDto(contestCreateDto)
                .setId(UUID.randomUUID())
                .setStatus(ContestStatus.OPEN_TO_SOLUTIONS);

        when(contestRepository.save(any(Contest.class))).thenReturn(contest);

        ApiAuthResponse<Contest> result = contestService.createContest(contestCreateDto);

        Assertions.assertEquals(result.getStatus().value(), 201);
        Assertions.assertEquals(result.getData().getStatus(), ContestStatus.OPEN_TO_SOLUTIONS);

        verify(contestRepository).save(any(Contest.class));
    }

    @Test
    public void getContestDetailById_TestValid() {
        UUID contestId = UUID.randomUUID();
        Set<SolutionMetaDto> solutions = new HashSet<>();
        Set<ContestCategory> categories = new HashSet<>(
                Set.of(new ContestCategory().setCategoryName("WEB"))
        );
        User user = new User();

        for (int i = 0; i < 10; i++) {
            solutions.add(new SolutionMetaDto().setName(String.valueOf(i)));
        }

        ContestDetailDto contest = new ContestDetailDto()
                .setId(contestId).setCategories(categories).setOwner(user);

        when(contestRepository.getContestDetailById(contestId))
                .thenReturn(Optional.ofNullable(contest));

        when(solutionRepository.getSolutionsMetaByContestId(contestId,10,null))
                .thenReturn(solutions);


        ApiAuthResponse<ContestDetailDto> result = contestService.getContest(contestId);

        Assertions.assertEquals(result.getStatus().value(), 200);
        Assertions.assertEquals(result.getData().getSolutions().size(), 10);
        Assertions.assertEquals(result.getData().getOwner(), user);
        Assertions.assertEquals(result.getData().getCategories(), categories);

        verify(contestRepository).getContestDetailById(contestId);
        verify(solutionRepository).getSolutionsMetaByContestId(contestId,10,null);
    }

    @Test
    public void updateContest_TestValid() {
        Contest contest = new Contest()
                .setId(UUID.randomUUID())
                .setStatus(ContestStatus.OPEN_TO_SOLUTIONS);

        Contest updatedContest = new Contest()
                .setId(contest.getId())
                .setStatus(ContestStatus.FINISHED);

        when(contestRepository.updateContest(contest)).thenReturn(updatedContest);

        ApiAuthResponse<Contest> result = contestService.updateContest(contest);

        Assertions.assertEquals(result.getStatus().value(), 200);
        Assertions.assertEquals(result.getData().getStatus(), ContestStatus.FINISHED);

        verify(contestRepository).updateContest(contest);
    }

    @Test
    public void deleteContest_TestValid() {
        UUID contestId = UUID.randomUUID();
        ApiAuthResponse<String> result = contestService.deleteContest(contestId);

        Assertions.assertEquals(result.getStatus().value(), 200);

        verify(contestRepository).deleteContestById(contestId);
    }
}
