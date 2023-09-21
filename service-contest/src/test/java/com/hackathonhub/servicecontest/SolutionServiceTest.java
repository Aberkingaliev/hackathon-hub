package com.hackathonhub.servicecontest;


import com.hackathonhub.servicecontest.constants.ApiSolutionResponseMessage;
import com.hackathonhub.servicecontest.dtos.ApiAuthResponse;
import com.hackathonhub.servicecontest.dtos.solution.SolutionCreateDto;
import com.hackathonhub.servicecontest.dtos.solution.SolutionMetaDto;
import com.hackathonhub.servicecontest.models.solution.Solution;
import com.hackathonhub.servicecontest.repositories.SolutionRepository;
import com.hackathonhub.servicecontest.services.SolutionService;
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
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith({MockitoExtension.class, SpringExtension.class})
public class SolutionServiceTest {

    @Mock
    private SolutionRepository solutionRepository;

    @InjectMocks
    @Autowired
    private SolutionService solutionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createSolution_TestValid() {
        SolutionCreateDto solutionCreateDto = new SolutionCreateDto();
        solutionCreateDto.setName("Test solution");
        Solution newSolution = Solution.fromCreateDto(solutionCreateDto);
        Solution createdSolution = new Solution()
                .setName("Test solution")
                .setId(UUID.randomUUID());

        when(solutionRepository.save(newSolution)).thenReturn(createdSolution);

        ApiAuthResponse<Solution> result =
                solutionService.createSolution(solutionCreateDto);

        Assertions.assertEquals(result.getData().getName(), "Test solution");
        Assertions.assertEquals(result.getStatus(), HttpStatus.CREATED);
        Assertions.assertEquals(result.getMessage(), ApiSolutionResponseMessage.SOLUTION_CREATED);

        verify(solutionRepository).save(newSolution);
    }

    @Test
    public void getSolutionById_TestValid() {
        UUID solutionId = UUID.randomUUID();
        Solution foundedSolution = new Solution()
                .setId(solutionId)
                .setName("Test solution");

        when(solutionRepository.findById(solutionId)).thenReturn(Optional.of(foundedSolution));

        ApiAuthResponse<Solution> result =
                solutionService.getSolutionById(solutionId);

        Assertions.assertEquals(result.getData().getId(), solutionId);
        Assertions.assertEquals(result.getStatus(), HttpStatus.OK);
        Assertions.assertEquals(result.getMessage(), ApiSolutionResponseMessage.SOLUTION_FOUND);

        verify(solutionRepository).findById(solutionId);
    }

    @Test
    public void getSolutionById_TestNotFound() {
        UUID solutionId = UUID.randomUUID();

        when(solutionRepository.findById(solutionId)).thenReturn(Optional.empty());

        ApiAuthResponse<Solution> result =
                solutionService.getSolutionById(solutionId);

        Assertions.assertEquals(result.getStatus(), HttpStatus.NOT_FOUND);
        Assertions.assertEquals(result.getMessage(), ApiSolutionResponseMessage.SOLUTION_NOT_FOUND);

        verify(solutionRepository).findById(solutionId);
    }

    @Test
    public void getSolutionMetaListByContestId_TestValid() {
        UUID contestId = UUID.randomUUID();
        Set<SolutionMetaDto> foundedSolutionMeta = new HashSet<>();

        for (int i=0; i<10; i++) {
            foundedSolutionMeta.add(new SolutionMetaDto()
                    .setId(UUID.randomUUID())
                    .setName("Test solution " + i));
        }

        when(solutionRepository.getSolutionMetaListById(contestId, 10, null)).thenReturn(foundedSolutionMeta);

        ApiAuthResponse<ArrayList<SolutionMetaDto>> result =
                solutionService.getSolutionMetaByContestId(contestId, 10, null);

        Assertions.assertEquals(result.getData().size(), 10);
        Assertions.assertEquals(result.getStatus(), HttpStatus.OK);
        Assertions.assertEquals(result.getMessage(), ApiSolutionResponseMessage.SOLUTION_FOUND);

        verify(solutionRepository).getSolutionMetaListById(contestId, 10, null);
    }

    @Test
    public void updateSolution_TestValid() {
        Solution solution = new Solution()
                .setId(UUID.randomUUID())
                .setName("Test solution");

        Solution updatedSolution = new Solution()
                .setId(UUID.randomUUID())
                .setName("Updated test solution");

        when(solutionRepository.findById(solution.getId())).thenReturn(Optional.of(solution));
        when(solutionRepository.update(solution)).thenReturn(updatedSolution);

        ApiAuthResponse<Solution> result =
                solutionService.updateSolution(solution);

        Assertions.assertEquals(result.getData().getId(), updatedSolution.getId());
        Assertions.assertEquals(result.getStatus(), HttpStatus.OK);
        Assertions.assertEquals(result.getMessage(), ApiSolutionResponseMessage.SOLUTION_UPDATED);

        verify(solutionRepository).findById(solution.getId());
        verify(solutionRepository).update(solution);
    }

    @Test
    public void updateSolution_TestNotFound() {
        Solution solution = new Solution()
                .setId(UUID.randomUUID())
                .setName("Test solution");

        when(solutionRepository.findById(solution.getId())).thenReturn(Optional.empty());

        ApiAuthResponse<Solution> result =
                solutionService.updateSolution(solution);

        Assertions.assertEquals(result.getStatus(), HttpStatus.NOT_FOUND);
        Assertions.assertEquals(result.getMessage(), ApiSolutionResponseMessage.SOLUTION_NOT_FOUND);

        verify(solutionRepository).findById(solution.getId());
    }

    @Test
    public void deleteSolution_TestValid() {
        UUID solutionId = UUID.randomUUID();

        ApiAuthResponse<String> result =
                solutionService.deleteSolution(solutionId);

        Assertions.assertEquals(result.getStatus(), HttpStatus.OK);
        Assertions.assertEquals(result.getMessage(), ApiSolutionResponseMessage.SOLUTION_DELETED);

        verify(solutionRepository).deleteById(solutionId);
    }
}
