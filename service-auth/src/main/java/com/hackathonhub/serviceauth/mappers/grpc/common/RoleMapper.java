package com.hackathonhub.serviceauth.mappers.grpc.common;

import com.hackathonhub.common.grpc.Entities;
import com.hackathonhub.serviceauth.models.Role;
import com.hackathonhub.serviceauth.models.RoleEnum;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RoleMapper {

    public static Set<Entities.Role> toGrpcEntity(Set<Role> roles) {
        return roles.stream()
                .map(role ->
                        Entities.Role.valueOf(role.getRole_name().toString()
                        )
                )
                .collect(Collectors.toSet());
    }

    public static HashSet<Role> toOriginalyRole(List<Entities.Role> roles) {
        return roles.stream()
                .map(role ->
                        new Role()
                                .setRole_name(RoleEnum.valueOf(role.toString()))
                )
                .collect(Collectors.toCollection(HashSet::new));
    }
}
