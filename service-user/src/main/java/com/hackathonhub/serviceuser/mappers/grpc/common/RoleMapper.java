package com.hackathonhub.serviceuser.mappers.grpc.common;

import com.hackathonhub.common.grpc.Entities;
import com.hackathonhub.serviceuser.models.Role;
import com.hackathonhub.serviceuser.models.RoleEnum;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RoleMapper {

    public static Set<Entities.Role> toGrpcEntity(Set<Role> roles) {
        return roles.stream()
                .map(role ->
                        Entities.Role.newBuilder()
                                .setId(TypeMapper.toGrpcUuid(role.getId()))
                                .setRole(Entities.RoleEnum.valueOf(role.getRoleName().toString()))
                                .build()
                )
                .collect(Collectors.toSet());
    }

    public static Set<Role> toOriginalyRole(List<Entities.Role> roles) {
        return roles.stream()
                .map(role ->
                        new Role()
                                .setId(TypeMapper.toOriginalyUuid(role.getId()))
                                .setRoleName(RoleEnum.valueOf(role.getRole().toString()))
                )
                .collect(Collectors.toCollection(HashSet::new));
    }
}
