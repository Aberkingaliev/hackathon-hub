package com.hackathonhub.serviceteam;

import com.hackathonhub.serviceteam.constants.ApiTeamMemberResponseMessage;
import com.hackathonhub.serviceteam.dto.ApiAuthResponse;
import com.hackathonhub.serviceteam.dto.MemberDto;
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

    private UUID teamId;
    private UUID userId;

    @BeforeEach
    void setup() {
        teamId = UUID.randomUUID();
        userId = UUID.randomUUID();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addMember_TestValid() {
        when(teamMemberRepository.findById(any(TeamMemberId.class)))
                .thenReturn(Optional.empty());
        when(teamMemberRepository.save(any(TeamMember.class)))
                .thenReturn(new TeamMember());

        ApiAuthResponse<String> result = teamMemberService.addMember(this.teamId, this.userId);

        verify(teamMemberRepository).findById(any(TeamMemberId.class));
        verify(teamMemberRepository).save(any(TeamMember.class));

        Assertions.assertEquals(HttpStatus.OK, result.getStatus());
        Assertions.assertEquals(
                ApiTeamMemberResponseMessage.USER_ADDED_TO_TEAM, result.getMessage()
        );
    }

    @Test
    public void addMember_TestAlreadyInTeam() {
        when(teamMemberRepository.findById(any(TeamMemberId.class)))
                .thenReturn(Optional.of(new TeamMember()));

        ApiAuthResponse<String> result = teamMemberService.addMember(this.teamId, this.userId);

        verify(teamMemberRepository).findById(any(TeamMemberId.class));
        verify(teamMemberRepository, times(0)).save(any(TeamMember.class));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        Assertions.assertEquals(
                ApiTeamMemberResponseMessage.USER_ALREADY_IN_TEAM, result.getMessage()
        );
    }


    @Test
    public void getMembers_TestValid () {
        UUID cursor = UUID.randomUUID();
        int limit = 10;

        Pageable pageRequest = PageRequest.of(0, limit);
        List<MemberDto> allMembers = List.of(new MemberDto());

        when(teamMemberRepository.findMembersByTeamId(this.teamId, cursor, pageRequest))
                .thenReturn(allMembers);

        ApiAuthResponse<HashSet<MemberDto>> result = teamMemberService
                .getMembers(this.teamId, cursor, limit);

        verify(teamMemberRepository).findMembersByTeamId(this.teamId, cursor, pageRequest);

        Assertions.assertEquals(HttpStatus.OK, result.getStatus());
        Assertions.assertEquals(
                ApiTeamMemberResponseMessage.MEMBERS_RECEIVED, result.getMessage()
        );
        Assertions.assertEquals(new HashSet<>(allMembers), result.getData());

    }

    @Test
    public void deleteMember_TestValid() {
        when(teamMemberRepository.findById(any(TeamMemberId.class)))
                .thenReturn(Optional.of(new TeamMember()));

        ApiAuthResponse<String> result = teamMemberService.deleteMember(this.teamId, this.userId);

        verify(teamMemberRepository).findById(any(TeamMemberId.class));
        verify(teamMemberRepository).deleteById(any(TeamMemberId.class));

        Assertions.assertEquals(HttpStatus.OK, result.getStatus());
    }

    @Test
    public void deleteMember_TestNotFound() {
        when(teamMemberRepository.findById(any(TeamMemberId.class)))
                .thenReturn(Optional.empty());

        ApiAuthResponse<String> result = teamMemberService.deleteMember(this.teamId, this.userId);

        verify(teamMemberRepository).findById(any(TeamMemberId.class));
        verify(teamMemberRepository, times(0))
                .deleteById(any(TeamMemberId.class));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        Assertions.assertEquals(
                ApiTeamMemberResponseMessage.USER_NOT_FOUND_IN_TEAM, result.getMessage()
        );
    }

}
