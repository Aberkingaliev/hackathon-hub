package com.hackathonhub.serviceauth.mappers.grpc.__mocks__;

import com.hackathonhub.serviceauth.models.Role;
import com.hackathonhub.serviceauth.models.RoleEnum;
import com.hackathonhub.serviceauth.models.User;

import java.util.HashSet;
import java.util.UUID;

public class UserMockLocal {

    public static User getUserForResponse() {
        HashSet<Role> mockRoles = new HashSet<>();
        mockRoles.add(
                new Role()
                        .setId(UUID.fromString("b294c989-7566-4de6-8255-20396315da38"))
                        .setRole_name(RoleEnum.ROLE_ADMIN)
        );
        return new User()
                .setId(UUID.fromString("b294c989-7566-4de6-8255-20396315da38"))
                .setUsername("johnDoe")
                .setFullName("John Doe")
                .setEmail("johnDoe@gmail.com")
                .setPassword("johnDoePassword")
                .setActivated(false)
                .setTeamId(UUID.fromString("b294c989-7566-4de6-8255-20396315da38"))
                .setRole(mockRoles);
    }

    public static User getUserForRequest() {
        HashSet<Role> mockRoles = new HashSet<>();
        mockRoles.add(
                new Role()
                        .setId(UUID.fromString("b294c989-7566-4de6-8255-20396315da38"))
                        .setRole_name(RoleEnum.ROLE_ADMIN)
        );
        return new User()
                .setUsername("johnDoe")
                .setFullName("John Doe")
                .setEmail("johnDoe@gmail.com")
                .setPassword("johnDoePassword")
                .setActivated(false)
                .setTeamId(UUID.fromString("b294c989-7566-4de6-8255-20396315da38"))
                .setRole(mockRoles);
    }

}
