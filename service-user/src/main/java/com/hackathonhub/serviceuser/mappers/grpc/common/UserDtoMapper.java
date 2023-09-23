package com.hackathonhub.serviceuser.mappers.grpc.common;

import com.hackathonhub.common.grpc.Dto;
import com.hackathonhub.serviceuser.dtos.UserDto;

public class UserDtoMapper {

    public static Dto.UserDto toGrpcDto(UserDto user) {
        return Dto.UserDto.newBuilder()
                .setId(user.getId().toString())
                .setUsername(user.getUsername())
                .setFullName(user.getFullName())
                .setEmail(user.getEmail())
                .setIsActivated(user.getIsActivated())
                .addAllRoles(RoleMapper.toGrpcEntity(user.getRoles()))
                .build();
    }
}
