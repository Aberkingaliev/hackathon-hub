package com.hackathonhub.serviceuser.services;

import com.hackathonhub.serviceuser.dtos.ApiAuthResponse;
import com.hackathonhub.serviceuser.models.Role;
import com.hackathonhub.serviceuser.repositories.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;


    public ApiAuthResponse<Role> createRole(Role role) {
        ApiAuthResponse.ApiAuthResponseBuilder<Role> responseBuilder =
                ApiAuthResponse.<Role>builder();
        try {
            Role savedRole = roleRepository.save(new Role().setRole_name(role.getRole_name()));

            return responseBuilder
                    .status(HttpStatus.CREATED)
                    .message("ROLE_CREATED")
                    .data(savedRole)
                    .build();
        } catch (Exception e) {
            log.error("Error while creating role " + e.getMessage());

            return responseBuilder
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error while creating role")
                    .build();
        }
    }
}
