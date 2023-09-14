package com.hackathonhub.serviceteam;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathonhub.serviceteam.constants.ApiTeamMemberResponseMessage;
import com.hackathonhub.serviceteam.controllers.TeamMemberController;
import com.hackathonhub.serviceteam.dto.ApiAuthResponse;
import com.hackathonhub.serviceteam.models.User;
import com.hackathonhub.serviceteam.services.TeamMemberService;
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

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({SpringExtension.class})
public class TeamMemberControllerTest {

    @Mock
    @Autowired
    private TeamMemberService teamMemberService;

    @InjectMocks
    @Autowired
    private TeamMemberController teamMemberController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(teamMemberController).build();
    }


    @Test
    public void addMember_TestValid() throws Exception {
        UUID teamId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        ApiAuthResponse<String> response = ApiAuthResponse.<String>builder()
                .status(HttpStatus.OK)
                .message(ApiTeamMemberResponseMessage.USER_ADDED_TO_TEAM)
                .build();

        String responseJson = objectMapper.writeValueAsString(response);


        when(teamMemberService.addMember(teamId, userId))
                .thenReturn(response);

        mockMvc.perform(post("/api/team/{id}/member", teamId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"userId\": \"" + userId + "\" }"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson));

        verify(teamMemberService).addMember(teamId, userId);
    }

    @Test
    public void addMember_TestAlreadyInTeam() throws Exception {
        UUID teamId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        ApiAuthResponse<String> response = ApiAuthResponse.<String>builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ApiTeamMemberResponseMessage.USER_ALREADY_IN_TEAM)
                .build();

        String responseJson = objectMapper.writeValueAsString(response);


        when(teamMemberService.addMember(teamId, userId))
                .thenReturn(response);

        mockMvc.perform(post("/api/team/{id}/member", teamId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"userId\": \"" + userId + "\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson));

        verify(teamMemberService).addMember(teamId, userId);
    }

    @Test
    public void getMembers_TestValid() throws Exception {
        UUID teamId = UUID.randomUUID();
        UUID cursor = UUID.randomUUID();
        ApiAuthResponse<HashSet<User>> response = ApiAuthResponse.<HashSet<User>>builder()
                .status(HttpStatus.OK)
                .message(ApiTeamMemberResponseMessage.MEMBERS_RECEIVED)
                .data(new HashSet<>(Set.of(new User())))
                .build();

        String responseJson = objectMapper.writeValueAsString(response);

        when(teamMemberService.getMembers(eq(teamId), any(UUID.class), any(Integer.class)))
                .thenReturn(response);

        mockMvc.perform(get("/api/team/{id}/member", teamId)
                        .param("cursor", cursor.toString())
                        .param("limit", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson));

        verify(teamMemberService).getMembers(eq(teamId), any(UUID.class), any(Integer.class));
    }

}
