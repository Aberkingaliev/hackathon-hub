package com.hackathonhub.serviceuser;

import com.hackathonhub.serviceuser.dtos.ApiAuthResponse;
import com.hackathonhub.serviceuser.models.Role;
import com.hackathonhub.serviceuser.models.RoleEnum;
import com.hackathonhub.serviceuser.repositories.RoleRepository;
import com.hackathonhub.serviceuser.services.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
@ExtendWith({MockitoExtension.class, SpringExtension.class})
public class RoleServiceTest {

    @Mock
    @Autowired
    RoleRepository roleRepository;

    @InjectMocks
    @Autowired
    RoleService roleService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getById_TestValid() {
        ApiAuthResponse<Role> roleResponse = RoleData.getRoleResponse_getById_Success();
        Role role = roleResponse.getData();

        when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));

        ApiAuthResponse<Role> foundedRole = roleService.getById(role.getId());

        verify(roleRepository, times(1)).findById(role.getId());

        Assertions.assertEquals(foundedRole, roleResponse);
    }

    @Test
    void getById_TestNotFound() {
        UUID id = UUID.randomUUID();
        ApiAuthResponse<Role> roleResponse = RoleData.getRoleResponse_NotFound();

        when(roleRepository.findById(id)).thenReturn(Optional.empty());

        ApiAuthResponse<Role> foundedRole = roleService.getById(id);

        verify(roleRepository, times(1)).findById(id);

        Assertions.assertEquals(roleResponse, foundedRole);
    }


    @Test
    void update_TestValid() {
        UUID id = UUID.randomUUID();
        Role role = new Role().setId(id).setRole_name(RoleEnum.ROLE_ADMIN);
        ApiAuthResponse<Role> roleResponse = RoleData.getRoleResponse_update_Success();

        when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
        when(roleRepository.save(any(Role.class))).thenReturn(roleResponse.getData());

        ApiAuthResponse<Role> updatedRoleResponse = roleService.update(role);

        verify(roleRepository, times(1)).findById(role.getId());
        verify(roleRepository, times(1)).save(any(Role.class));

        Assertions.assertEquals(updatedRoleResponse, roleResponse);
    }

    @Test
    void update_TestNotFound() {
        Role role = RoleData.getRole();
        ApiAuthResponse<Role> roleResponse = RoleData.getRoleResponse_NotFound();

        when(roleRepository.findById(role.getId())).thenReturn(Optional.empty());

        ApiAuthResponse<Role> updatedRoleResponse = roleService.update(role);

        verify(roleRepository, times(1)).findById(role.getId());

        Assertions.assertEquals(updatedRoleResponse, roleResponse);
    }

    @Test
    void delete_TestValid() {
        Role role = RoleData.getRole();
        ApiAuthResponse<Role> roleResponse = RoleData.getRoleResponse_delete_Success();

        when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));

        ApiAuthResponse<Role> deletedRoleResponse = roleService.delete(role);

        verify(roleRepository, times(1)).findById(role.getId());
        verify(roleRepository, times(1)).delete(role);

        Assertions.assertEquals(deletedRoleResponse, roleResponse);
    }

    @Test
    void delete_TestNotFound() {
        Role role = RoleData.getRole();
        ApiAuthResponse<Role> roleResponse = RoleData.getRoleResponse_NotFound();

        when(roleRepository.findById(role.getId())).thenReturn(Optional.empty());

        ApiAuthResponse<Role> deletedRoleResponse = roleService.delete(role);

        verify(roleRepository, times(1)).findById(role.getId());

        Assertions.assertEquals(deletedRoleResponse, roleResponse);
    }

}
