package com.hackathonhub.servicecontest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathonhub.servicecontest.constants.ApiContestResponseMessage;
import com.hackathonhub.servicecontest.controllers.ContestController;
import com.hackathonhub.servicecontest.dtos.ApiAuthResponse;
import com.hackathonhub.servicecontest.dtos.contest.ContestCreateDto;
import com.hackathonhub.servicecontest.dtos.contest.ContestDetailDto;
import com.hackathonhub.servicecontest.models.contest.Contest;
import com.hackathonhub.servicecontest.services.ContestService;
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

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({MockitoExtension.class, SpringExtension.class})
public class ContestControllerTest {

    @Mock
    private ContestService contestService;

    @InjectMocks
    @Autowired
    private ContestController contestController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(contestController).build();
    }

    @Test
    public void createContest_TestValid() throws Exception {
        Contest contest = new Contest();
        ContestCreateDto contestCreateDto = new ContestCreateDto();
        ApiAuthResponse<Contest> contestResponse = ApiAuthResponse.
                <Contest>builder()
                .status(HttpStatus.CREATED)
                .message(ApiContestResponseMessage.CONTEST_CREATED)
                .data(contest)
                .build();

        when(contestService.createContest(any(ContestCreateDto.class))).thenReturn(contestResponse);

        mockMvc.perform(
                post("/api/contest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contestCreateDto))
                )
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(contestResponse)));

        verify(contestService).createContest(any(ContestCreateDto.class));
    }

    @Test
    public void getContestDetailById_TestValid() throws Exception {
        ContestDetailDto contest = new ContestDetailDto();
        ApiAuthResponse<ContestDetailDto> contestResponse = ApiAuthResponse.
                <ContestDetailDto>builder()
                .status(HttpStatus.OK)
                .message(ApiContestResponseMessage.CONTEST_FOUND)
                .data(contest)
                .build();

        when(contestService.getContest(any(UUID.class))).thenReturn(contestResponse);

        mockMvc.perform(
                get("/api/contest/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(contestResponse)));

        verify(contestService).getContest(any(UUID.class));
    }

    @Test
    public void updateContest_TestValid() throws Exception {
        Contest contest = new Contest();
        ApiAuthResponse<Contest> contestResponse = ApiAuthResponse.
                <Contest>builder()
                .status(HttpStatus.OK)
                .message(ApiContestResponseMessage.CONTEST_UPDATED)
                .data(contest)
                .build();

        when(contestService.updateContest(any(Contest.class))).thenReturn(contestResponse);

        mockMvc.perform(
                put("/api/contest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contest))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(contestResponse)));

        verify(contestService).updateContest(any(Contest.class));
    }

    @Test
    public void deleteContest_TestValid() throws Exception {
        ApiAuthResponse<String> contestResponse = ApiAuthResponse.
                <String>builder()
                .status(HttpStatus.OK)
                .message(ApiContestResponseMessage.CONTEST_DELETED)
                .build();

        when(contestService.deleteContest(any(UUID.class)))
                .thenReturn(contestResponse);

        mockMvc.perform(
                delete("/api/contest/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(contestService).deleteContest(any(UUID.class));
    }
}
