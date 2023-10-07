package com.hackathonhub.serviceidentity.mappers.grpc.common;

import com.hackathonhub.common.grpc.Dto;
import com.hackathonhub.serviceidentity.dtos.UserDto;

import java.util.UUID;


public class UserDtoMapper {


    public static UserDto toOriginalDto(Dto.UserDto user) {
        return new UserDto()
                .setId(UUID.fromString(user.getId()))
                .setUsername(user.getUsername())
                .setFullName(user.getFullName())
                .setEmail(user.getEmail())
                .setIsActivated(user.getIsActivated())
                .setRoles(RoleMapper.toOriginalyRole(user.getRolesList()));
    }
}
