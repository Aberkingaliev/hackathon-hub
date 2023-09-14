package com.hackathonhub.serviceuser.services;

import com.hackathonhub.serviceuser.constants.ApiRoleResponseMessage;
import com.hackathonhub.serviceuser.dtos.ApiAuthResponse;
import com.hackathonhub.serviceuser.models.Role;
import com.hackathonhub.serviceuser.repositories.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;


    public ApiAuthResponse<Role> create(Role role) {
        ApiAuthResponse.ApiAuthResponseBuilder<Role> responseBuilder =
                ApiAuthResponse.<Role>builder();
        try {
            Role savedRole = roleRepository.save(new Role().setRole_name(role.getRole_name()));

            return responseBuilder
                    .status(HttpStatus.CREATED)
                    .message(ApiRoleResponseMessage.ROLE_CREATED)
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

    public ApiAuthResponse<Role> getById(UUID id) {
        ApiAuthResponse.ApiAuthResponseBuilder<Role> responseBuilder =
                ApiAuthResponse.<Role>builder();

        try {
            Role foundedRole = roleRepository.getById(id);

            if(foundedRole.getId() == null) {

                return responseBuilder
                        .status(HttpStatus.NOT_FOUND)
                        .message(ApiRoleResponseMessage.ROLE_NOT_FOUND)
                        .build();
            }

            return responseBuilder
                    .status(HttpStatus.OK)
                    .message(ApiRoleResponseMessage.ROLE_FOUND)
                    .data(foundedRole)
                    .build();

        } catch (Exception e) {
            return responseBuilder
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("ERROR_WHILE_GETTING_ROLE")
                    .build();
        }
    }

    public ApiAuthResponse<Role> update(Role role) {
        ApiAuthResponse.ApiAuthResponseBuilder<Role> responseBuilder =
                ApiAuthResponse.<Role>builder();
        try {
            Optional<Role> foundedRole = roleRepository.findById(role.getId());

            if(foundedRole.isEmpty()) {
                return responseBuilder
                        .status(HttpStatus.NOT_FOUND)
                        .message(ApiRoleResponseMessage.ROLE_NOT_FOUND)
                        .build();
            }

            Role updatedRole = foundedRole.get().setRole_name(role.getRole_name());
            Role savedRole = roleRepository.save(updatedRole);

            return responseBuilder
                    .status(HttpStatus.OK)
                    .message(ApiRoleResponseMessage.ROLE_UPDATED)
                    .data(savedRole)
                    .build();
        } catch (Exception e) {
            log.error("Error while updating role " + e.getMessage());

            return responseBuilder
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error while updating role")
                    .build();
        }
    }

    public ApiAuthResponse<Role> delete(Role role) {

        ApiAuthResponse.ApiAuthResponseBuilder<Role> responseBuilder =
                ApiAuthResponse.<Role>builder();
        try {
            Optional<Role> foundedRole = roleRepository.findById(role.getId());

            if(foundedRole.isEmpty()) {
                return responseBuilder
                        .status(HttpStatus.NOT_FOUND)
                        .message(ApiRoleResponseMessage.ROLE_NOT_FOUND)
                        .build();
            }

            roleRepository.delete(foundedRole.get());

            return responseBuilder
                    .status(HttpStatus.OK)
                    .message(ApiRoleResponseMessage.ROLE_DELETED)
                    .build();
        } catch (Exception e) {
            log.error("Error while deleting role " + e.getMessage());

            return responseBuilder
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error while deleting role")
                    .build();
        }
    }
}