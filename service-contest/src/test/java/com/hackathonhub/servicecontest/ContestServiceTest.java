package com.hackathonhub.servicecontest;


import com.hackathonhub.servicecontest.dtos.ApiAuthResponse;
import com.hackathonhub.servicecontest.dtos.contest.ContestCreateDto;
import com.hackathonhub.servicecontest.dtos.contest.ContestDetailDto;
import com.hackathonhub.servicecontest.dtos.contest.ContestUpdateDto;
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

import java.io.Serializable;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith({MockitoExtension.class, SpringExtension.class})
class ContestServiceTest {

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
    void createContest_TestValid() throws Exception {
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

        Assertions.assertEquals(201, result.getStatus().value());
        Assertions.assertEquals(ContestStatus.OPEN_TO_SOLUTIONS, result.getData().get().getStatus());

        verify(contestRepository).save(any(Contest.class));
    }

    @Test
    void getContestDetailById_TestValid() {
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

        when(contestRepository.getDetailById(contestId))
                .thenReturn(Optional.ofNullable(contest));

        when(solutionRepository.getSolutionMetaListById(contestId,10,null))
                .thenReturn(solutions);


        ApiAuthResponse<ContestDetailDto> result = contestService.getContest(contestId);

        Assertions.assertEquals(200, result.getStatus().value());
        Assertions.assertEquals(10, result.getData().get().getSolutions().size());
        Assertions.assertEquals(user, result.getData().get().getOwner());
        Assertions.assertEquals(categories, result.getData().get().getCategories());

        verify(contestRepository).getDetailById(contestId);
        verify(solutionRepository).getSolutionMetaListById(contestId,10,null);
    }

    @Test
    void updateContest_TestValid() {
        UUID id = UUID.randomUUID();
        ContestUpdateDto contest = new ContestUpdateDto()
                .setId(id)
                .setStatus(ContestStatus.OPEN_TO_SOLUTIONS);

        Contest foundedContest = new Contest()
                .setId(id)
                .setStatus(ContestStatus.OPEN_TO_SOLUTIONS)
                .setName("name");

        Contest updatedContest = new Contest()
                .setId(id)
                .setStatus(ContestStatus.FINISHED)
                .setName("name");

        when(contestRepository.findById(contest.getId())).thenReturn(Optional.of(foundedContest));
        when(contestRepository.update(foundedContest)).thenReturn(updatedContest);

        ApiAuthResponse<Contest> result = contestService.updateContest(contest);

        Assertions.assertEquals(200, result.getStatus().value());
        Assertions.assertEquals(ContestStatus.FINISHED, result.getData().get().getStatus());

        verify(contestRepository).findById(contest.getId());
        verify(contestRepository).update(foundedContest);
    }

    @Test
    void deleteContest_TestValid() {
        UUID contestId = UUID.randomUUID();
        ApiAuthResponse<Serializable> result = contestService.deleteContest(contestId);

        Assertions.assertEquals(200, result.getStatus().value());

        verify(contestRepository).deleteById(contestId);
    }
}
