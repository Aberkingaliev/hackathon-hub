package com.hackathonhub.servicecontest;


import com.hackathonhub.servicecontest.constants.ApiSolutionResponseMessage;
import com.hackathonhub.servicecontest.dtos.ApiAuthResponse;
import com.hackathonhub.servicecontest.dtos.solution.SolutionCreateDto;
import com.hackathonhub.servicecontest.dtos.solution.SolutionMetaDto;
import com.hackathonhub.servicecontest.dtos.solution.SolutionUpdateDto;
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

import java.io.Serializable;
import java.util.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith({MockitoExtension.class, SpringExtension.class})
class SolutionServiceTest {

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
    void createSolution_TestValid() {
        SolutionCreateDto solutionCreateDto = new SolutionCreateDto();
        solutionCreateDto.setName("Test solution");
        Solution newSolution = Solution.fromCreateDto(solutionCreateDto);
        Solution createdSolution = new Solution()
                .setName("Test solution")
                .setId(UUID.randomUUID());

        when(solutionRepository.save(newSolution)).thenReturn(createdSolution);

        ApiAuthResponse<Solution> result =
                solutionService.createSolution(solutionCreateDto);

        Assertions.assertEquals("Test solution", result.getData().get().getName());
        Assertions.assertEquals(HttpStatus.CREATED, result.getStatus());
        Assertions.assertEquals(ApiSolutionResponseMessage.SOLUTION_CREATED, result.getMessage());

        verify(solutionRepository).save(newSolution);
    }

    @Test
    void getSolutionById_TestValid() {
        UUID solutionId = UUID.randomUUID();
        Solution foundedSolution = new Solution()
                .setId(solutionId)
                .setName("Test solution");

        when(solutionRepository.findById(solutionId)).thenReturn(Optional.of(foundedSolution));

        ApiAuthResponse<Solution> result =
                solutionService.getSolutionById(solutionId);

        Assertions.assertEquals(solutionId, result.getData().get().getId());
        Assertions.assertEquals(HttpStatus.OK, result.getStatus());
        Assertions.assertEquals(ApiSolutionResponseMessage.SOLUTION_FOUND, result.getMessage());

        verify(solutionRepository).findById(solutionId);
    }

    @Test
    void getSolutionById_TestNotFound() {
        UUID solutionId = UUID.randomUUID();

        when(solutionRepository.findById(solutionId)).thenReturn(Optional.empty());

        ApiAuthResponse<Solution> result =
                solutionService.getSolutionById(solutionId);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatus());
        Assertions.assertEquals(ApiSolutionResponseMessage.SOLUTION_NOT_FOUND, result.getMessage());

        verify(solutionRepository).findById(solutionId);
    }

    @Test
    void getSolutionMetaListByContestId_TestValid() {
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

        Assertions.assertEquals(10, result.getData().get().size());
        Assertions.assertEquals(HttpStatus.OK, result.getStatus());
        Assertions.assertEquals(ApiSolutionResponseMessage.SOLUTION_FOUND, result.getMessage());

        verify(solutionRepository).getSolutionMetaListById(contestId, 10, null);
    }

    @Test
    void updateSolution_TestValid() {
        SolutionUpdateDto solution = new SolutionUpdateDto()
                .setId(UUID.randomUUID())
                .setName("Test solution");

        Solution foundedSolution = new Solution()
                .setId(solution.getId())
                .setName("Test solution");

        Solution updatedSolution = new Solution()
                .setId(UUID.randomUUID())
                .setName("Updated test solution");

        when(solutionRepository.findById(solution.getId())).thenReturn(Optional.of(foundedSolution));
        when(solutionRepository.update(foundedSolution.setName("Updated test solution"))).thenReturn(updatedSolution);

        ApiAuthResponse<Solution> result =
                solutionService.updateSolution(solution);

        Assertions.assertEquals(updatedSolution.getId(), result.getData().get().getId());
        Assertions.assertEquals(HttpStatus.OK, result.getStatus());
        Assertions.assertEquals(ApiSolutionResponseMessage.SOLUTION_UPDATED, result.getMessage());

        verify(solutionRepository).findById(solution.getId());
        verify(solutionRepository).update(foundedSolution.setName("Updated test solution"));
    }

    @Test
    void updateSolution_TestNotFound() {
        SolutionUpdateDto solution = new SolutionUpdateDto()
                .setId(UUID.randomUUID())
                .setName("Test solution");

        when(solutionRepository.findById(solution.getId())).thenReturn(Optional.empty());

        ApiAuthResponse<Solution> result =
                solutionService.updateSolution(solution);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatus());
        Assertions.assertEquals(ApiSolutionResponseMessage.SOLUTION_NOT_FOUND, result.getMessage());

        verify(solutionRepository).findById(solution.getId());
    }

    @Test
    void deleteSolution_TestValid() {
        UUID solutionId = UUID.randomUUID();

        ApiAuthResponse<Serializable> result =
                solutionService.deleteSolution(solutionId);

        Assertions.assertEquals(HttpStatus.OK, result.getStatus());
        Assertions.assertEquals(ApiSolutionResponseMessage.SOLUTION_DELETED, result.getMessage());

        verify(solutionRepository).deleteById(solutionId);
    }
}
