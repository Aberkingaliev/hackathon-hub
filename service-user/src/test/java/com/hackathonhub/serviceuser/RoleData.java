package com.hackathonhub.serviceuser;


import com.hackathonhub.serviceuser.constants.ApiRoleResponseMessage;
import com.hackathonhub.serviceuser.dtos.ApiAuthResponse;
import com.hackathonhub.serviceuser.models.Role;
import com.hackathonhub.serviceuser.models.RoleEnum;
import org.springframework.http.HttpStatus;

import java.util.UUID;

/*

    Required data for role tests

 */
public class RoleData {

    public static ApiAuthResponse<Role> getRoleResponse_getById_Success () {
        return ApiAuthResponse.<Role>builder()
                .status(HttpStatus.OK)
                .message(ApiRoleResponseMessage.ROLE_FOUND)
                .data(getRole())
                .build();
    }

    public static ApiAuthResponse<Role> getRoleResponse_update_Success () {
        return ApiAuthResponse.<Role>builder()
                .status(HttpStatus.OK)
                .message(ApiRoleResponseMessage.ROLE_UPDATED)
                .data(getRole())
                .build();
    }

    public static ApiAuthResponse<Role> getRoleResponse_delete_Success () {
        return ApiAuthResponse.<Role>builder()
                .status(HttpStatus.OK)
                .message(ApiRoleResponseMessage.ROLE_DELETED)
                .build();
    }

    public static ApiAuthResponse<Role> getRoleResponse_NotFound () {
        return ApiAuthResponse.<Role>builder()
                .status(HttpStatus.NOT_FOUND)
                .message(ApiRoleResponseMessage.ROLE_NOT_FOUND)
                .build();
    }

    public static Role getRole() {
        return new Role()
                .setId(UUID.randomUUID())
                .setRole_name(RoleEnum.ROLE_ADMIN);
    }
}
