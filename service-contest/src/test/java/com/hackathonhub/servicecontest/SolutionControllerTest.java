package com.hackathonhub.servicecontest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.hackathonhub.servicecontest.constants.ApiSolutionResponseMessage;
import com.hackathonhub.servicecontest.controllers.SolutionController;
import com.hackathonhub.servicecontest.dtos.ApiAuthResponse;
import com.hackathonhub.servicecontest.dtos.solution.SolutionCreateDto;
import com.hackathonhub.servicecontest.dtos.solution.SolutionUpdateDto;
import com.hackathonhub.servicecontest.models.solution.Solution;
import com.hackathonhub.servicecontest.services.SolutionService;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({MockitoExtension.class, SpringExtension.class})
class SolutionControllerTest {

    @Mock
    private SolutionService solutionService;

    @InjectMocks
    @Autowired
    private SolutionController solutionController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new Jdk8Module());

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(solutionController).build();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createSolution_TestValid() throws Exception {
        SolutionCreateDto solutionCreateDto = new SolutionCreateDto();
        Solution createdSolution = new Solution();
        ApiAuthResponse<Solution> solutionResponse = ApiAuthResponse.<Solution>builder()
                .status(HttpStatus.CREATED)
                .data(Optional.of(createdSolution))
                .message(ApiSolutionResponseMessage.SOLUTION_CREATED)
                .build();

        when(solutionService.createSolution(solutionCreateDto)).thenReturn(solutionResponse);

        mockMvc.perform(post("/api/solution")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(solutionCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(solutionResponse)));

        verify(solutionService).createSolution(solutionCreateDto);
    }

    @Test
    void getSolutionById_TestValid() throws Exception {
        UUID id = UUID.randomUUID();
        Solution solution = new Solution().setId(id);
        ApiAuthResponse<Solution> solutionResponse = ApiAuthResponse.<Solution>builder()
                .status(HttpStatus.OK)
                .data(Optional.ofNullable(solution))
                .message(ApiSolutionResponseMessage.SOLUTION_FOUND)
                .build();

        when(solutionService.getSolutionById(solution.getId())).thenReturn(solutionResponse);

        mockMvc.perform(get("/api/solution/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(solutionResponse)));

        verify(solutionService).getSolutionById(solution.getId());
    }

    @Test
    void getSolutionById_TestNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        ApiAuthResponse<Solution> solutionResponse = ApiAuthResponse.<Solution>builder()
                .status(HttpStatus.NOT_FOUND)
                .message(ApiSolutionResponseMessage.SOLUTION_NOT_FOUND)
                .build();

        when(solutionService.getSolutionById(id)).thenReturn(solutionResponse);

        mockMvc.perform(get("/api/solution/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(solutionResponse)));

        verify(solutionService).getSolutionById(id);
    }

    @Test
    void updateSolution_TestValid() throws Exception {
        SolutionUpdateDto solution = new SolutionUpdateDto().setName("name");
        Solution updatedSolution = new Solution().setName("updatedName");

        ApiAuthResponse<Solution> solutionResponse = ApiAuthResponse.<Solution>builder()
                .status(HttpStatus.OK)
                .data(Optional.ofNullable(updatedSolution))
                .message(ApiSolutionResponseMessage.SOLUTION_UPDATED)
                .build();

        when(solutionService.updateSolution(solution)).thenReturn(solutionResponse);

        mockMvc.perform(put("/api/solution")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(solution)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(solutionResponse)));

        verify(solutionService).updateSolution(solution);
    }

    @Test
    void updateSolution_TestNotFound() throws Exception {
        SolutionUpdateDto solution = new SolutionUpdateDto().setId(UUID.randomUUID());
        ApiAuthResponse<Solution> solutionResponse = ApiAuthResponse.<Solution>builder()
                .status(HttpStatus.NOT_FOUND)
                .message(ApiSolutionResponseMessage.SOLUTION_NOT_FOUND)
                .build();

        when(solutionService.updateSolution(solution)).thenReturn(solutionResponse);

        mockMvc.perform(put("/api/solution")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(solution)))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(solutionResponse)));

        verify(solutionService).updateSolution(solution);
    }

    @Test
    void deleteSolution_TestValid() throws Exception {
        UUID id = UUID.randomUUID();
        ApiAuthResponse<Serializable> solutionResponse = ApiAuthResponse.<Serializable>builder()
                .status(HttpStatus.OK)
                .message(ApiSolutionResponseMessage.SOLUTION_DELETED)
                .build();

        when(solutionService.deleteSolution(id)).thenReturn(solutionResponse);

        mockMvc.perform(delete("/api/solution/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(solutionResponse)));

        verify(solutionService).deleteSolution(id);
    }

}
