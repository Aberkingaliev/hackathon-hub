package com.hackathonhub.servicecontest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathonhub.servicecontest.controllers.ContestController;
import com.hackathonhub.servicecontest.dtos.ApiAuthResponse;
import com.hackathonhub.servicecontest.models.contest.Contest;
import com.hackathonhub.servicecontest.services.ContestService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

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

    @Test
    private void createContest_TestValid() throws Exception {
        ApiAuthResponse<Contest> contestResponse;
    }
}
