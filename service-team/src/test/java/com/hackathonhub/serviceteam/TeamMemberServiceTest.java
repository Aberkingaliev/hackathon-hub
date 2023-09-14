package com.hackathonhub.serviceteam;

import com.hackathonhub.serviceteam.constants.ApiTeamMemberResponseMessage;
import com.hackathonhub.serviceteam.dto.ApiAuthResponse;
import com.hackathonhub.serviceteam.models.TeamMember;
import com.hackathonhub.serviceteam.models.TeamMemberId;
import com.hackathonhub.serviceteam.repositories.TeamMemberRepository;
import com.hackathonhub.serviceteam.services.TeamMemberService;
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

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
@ExtendWith({MockitoExtension.class, SpringExtension.class})
public class TeamMemberServiceTest {

    @Mock
    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @InjectMocks
    @Autowired
    private TeamMemberService teamMemberService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addMember_TestValid() {
        UUID team_id = UUID.randomUUID();
        UUID user_id = UUID.randomUUID();
        TeamMemberId newTeamMemberId = new TeamMemberId();
        newTeamMemberId.setTeamId(team_id);
        newTeamMemberId.setUserId(user_id);

        when(teamMemberRepository.save(any(TeamMember.class)))
                .thenReturn(new TeamMember());

        ApiAuthResponse<String> result = teamMemberService.addMember(newTeamMemberId);

        verify(teamMemberRepository).save(any(TeamMember.class));

        Assertions.assertEquals(HttpStatus.OK, result.getStatus());
        Assertions.assertEquals(ApiTeamMemberResponseMessage.USER_ADDED_TO_TEAM, result.getMessage());
    }

}
