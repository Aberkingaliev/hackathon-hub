package com.hackathonhub.serviceuser;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathonhub.serviceuser.controllers.RoleController;
import com.hackathonhub.serviceuser.dtos.ApiAuthResponse;
import com.hackathonhub.serviceuser.models.Role;
import com.hackathonhub.serviceuser.services.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith({MockitoExtension.class, SpringExtension.class})
public class RoleControllerTest {

    @Mock
    private RoleService roleService;
    @InjectMocks
    @Autowired
    private RoleController roleController;
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();
    }


    @Test
    void getById_TestValid() throws Exception {
         ApiAuthResponse<Role> roleResponse = RoleData.getRoleResponse_getById_Success();
         UUID id = roleResponse.getData().getId();

         String roleResponseJson = objectMapper.writeValueAsString(roleResponse);

         when(roleService.getById(id)).thenReturn(roleResponse);

         mockMvc.perform(get("/api/role/{id}", id))
                 .andExpect(status().isOk())
                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                 .andExpect(content().json(roleResponseJson))
         ;
    }

    @Test
    void getById_TestNotFound() throws Exception {
        ApiAuthResponse<Role> roleResponse = RoleData.getRoleResponse_NotFound();
        UUID id = UUID.randomUUID();

        String roleResponseJson = objectMapper.writeValueAsString(roleResponse);

        when(roleService.getById(id)).thenReturn(roleResponse);

        mockMvc.perform(get("/api/role/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(roleResponseJson))
        ;
    }

    @Test
    void update_TestValid() throws Exception {
        ApiAuthResponse<Role> roleResponse = RoleData.getRoleResponse_update_Success();

        String roleJson = objectMapper.writeValueAsString(roleResponse.getData());
        String roleResponseJson = objectMapper.writeValueAsString(roleResponse);

        when(roleService.update(any(Role.class))).thenReturn(roleResponse);

        mockMvc.perform(put("/api/role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(roleJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(roleResponseJson));
    }

    @Test
    void update_TestNotFound() throws Exception {
        ApiAuthResponse<Role> roleResponse = RoleData.getRoleResponse_NotFound();
        Role missignRole = new Role();

        String roleJson = objectMapper.writeValueAsString(missignRole);
        String roleResponseJson = objectMapper.writeValueAsString(roleResponse);

        when(roleService.update(missignRole)).thenReturn(roleResponse);

        mockMvc.perform(put("/api/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(roleJson))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(roleResponseJson));
    }

    @Test
    void delete_TestValid() throws Exception {
        ApiAuthResponse<Role> roleResponse = RoleData.getRoleResponse_delete_Success();
        Role role = RoleData.getRole();

        String roleJson = objectMapper.writeValueAsString(role);
        String roleResponseJson = objectMapper.writeValueAsString(roleResponse);

        when(roleService.delete(role)).thenReturn(roleResponse);

        mockMvc.perform(delete("/api/role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(roleJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(roleResponseJson));
    }

    @Test
    void delete_TestNotFound() throws Exception {
        ApiAuthResponse<Role> roleResponse = RoleData.getRoleResponse_NotFound();
        Role missingRole = new Role();

        String roleJson = objectMapper.writeValueAsString(missingRole);
        String roleResponseJson = objectMapper.writeValueAsString(roleResponse);

        when(roleService.delete(missingRole)).thenReturn(roleResponse);

        mockMvc.perform(delete("/api/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(roleJson))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(roleResponseJson));
    }
}
