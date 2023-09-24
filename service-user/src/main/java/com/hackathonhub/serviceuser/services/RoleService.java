package com.hackathonhub.serviceuser.services;

import com.hackathonhub.serviceuser.constants.ApiRoleResponseMessage;
import com.hackathonhub.serviceuser.dtos.ApiAuthResponse;
import com.hackathonhub.serviceuser.models.Role;
import com.hackathonhub.serviceuser.repositories.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;


    public ApiAuthResponse<Role> create(Role role) {
        ApiAuthResponse<Role> responseBuilder = new ApiAuthResponse<>();
        try {
            Role savedRole = roleRepository.save(new Role().setRoleName(role.getRoleName()));
            return responseBuilder.created(savedRole, ApiRoleResponseMessage.ROLE_CREATED);
        } catch (Exception e) {
            log.error("Error while creating role " + e.getMessage());
            return responseBuilder.internalServerError(e.getMessage());
        }
    }

    public ApiAuthResponse<Role> getById(UUID id) {
        ApiAuthResponse<Role> responseBuilder = new ApiAuthResponse<>();

        try {
            Optional<Role> foundedRole = roleRepository.findById(id);
            return foundedRole.map(role -> responseBuilder.ok(role, ApiRoleResponseMessage.ROLE_FOUND))
                    .orElseGet(() -> responseBuilder.notFound(ApiRoleResponseMessage.ROLE_NOT_FOUND));
        } catch (Exception e) {
            return responseBuilder.internalServerError(e.getMessage());
        }
    }

    public ApiAuthResponse<Role> update(Role role) {
        ApiAuthResponse<Role> responseBuilder = new ApiAuthResponse<>();
        try {
            Optional<Role> foundedRole = roleRepository.findById(role.getId());
            return foundedRole.map(r -> {
                r.setRoleName(role.getRoleName());
                Role updatedRole = roleRepository.save(r);
                return responseBuilder.ok(updatedRole, ApiRoleResponseMessage.ROLE_UPDATED);
            }).orElseGet(() -> responseBuilder.notFound(ApiRoleResponseMessage.ROLE_NOT_FOUND));
        } catch (Exception e) {
            log.error("Error while updating role " + e.getMessage());
            return responseBuilder.internalServerError(e.getMessage());
        }
    }

    public ApiAuthResponse<Role> delete(Role role) {
        ApiAuthResponse<Role> responseBuilder = new ApiAuthResponse<>();
        try {
            Optional<Role> foundedRole = roleRepository.findById(role.getId());
            return foundedRole.map(r -> {
                roleRepository.delete(r);
                return responseBuilder.ok(ApiRoleResponseMessage.ROLE_DELETED);
            }).orElseGet(() -> responseBuilder.notFound(ApiRoleResponseMessage.ROLE_NOT_FOUND));
        } catch (Exception e) {
            log.error("Error while deleting role " + e.getMessage());
            return responseBuilder.internalServerError(e.getMessage());
        }
    }
}
