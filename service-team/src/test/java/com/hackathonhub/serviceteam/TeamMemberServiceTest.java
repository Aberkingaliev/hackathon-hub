package com.hackathonhub.serviceteam;

import com.hackathonhub.serviceteam.constants.ApiTeamMemberResponseMessage;
import com.hackathonhub.serviceteam.dto.ApiAuthResponse;
import com.hackathonhub.serviceteam.models.TeamMember;
import com.hackathonhub.serviceteam.models.TeamMemberId;
import com.hackathonhub.serviceteam.models.User;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
@ExtendWith({SpringExtension.class})
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
        UUID teamId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(teamMemberRepository.findById(any(TeamMemberId.class)))
                .thenReturn(Optional.empty());
        when(teamMemberRepository.save(any(TeamMember.class)))
                .thenReturn(new TeamMember());

        ApiAuthResponse<String> result = teamMemberService.addMember(teamId, userId);

        verify(teamMemberRepository).findById(any(TeamMemberId.class));
        verify(teamMemberRepository).save(any(TeamMember.class));

        Assertions.assertEquals(HttpStatus.OK, result.getStatus());
        Assertions.assertEquals(ApiTeamMemberResponseMessage.USER_ADDED_TO_TEAM, result.getMessage());
    }

    @Test
    public void addMember_TestAlreadyInTeam() {
        UUID teamId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(teamMemberRepository.findById(any(TeamMemberId.class)))
                .thenReturn(Optional.of(new TeamMember()));

        ApiAuthResponse<String> result = teamMemberService.addMember(teamId, userId);

        verify(teamMemberRepository).findById(any(TeamMemberId.class));
        verify(teamMemberRepository, times(0)).save(any(TeamMember.class));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        Assertions.assertEquals(ApiTeamMemberResponseMessage.USER_ALREADY_IN_TEAM, result.getMessage());
    }


    @Test
    public void getMembers_TestValid () {
        UUID team_id = UUID.randomUUID();
        UUID cursor = UUID.randomUUID();
        int limit = 10;

        Pageable pageRequest = PageRequest.of(0, limit);
        List<User> allMembers = List.of(new User());

        when(teamMemberRepository.findMembersByTeamId(team_id, cursor, pageRequest)).thenReturn(allMembers);

        ApiAuthResponse<HashSet<User>> result = teamMemberService.getMembers(team_id, cursor, limit);

        verify(teamMemberRepository).findMembersByTeamId(team_id, cursor, pageRequest);

        Assertions.assertEquals(HttpStatus.OK, result.getStatus());
        Assertions.assertEquals(ApiTeamMemberResponseMessage.MEMBERS_RECEIVED, result.getMessage());
        Assertions.assertEquals(new HashSet<>(allMembers), result.getData());

    }

}
