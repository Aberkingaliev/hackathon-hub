package com.hackathonhub.serviceteam.mappers.grpc.common;

import com.hackathonhub.common.grpc.Types;

import java.util.UUID;

public class TypeMapper {


    public static Types.UUID toGrpcUuid(UUID uuid) {
        if(uuid == null) return null;
        return Types.UUID
                .newBuilder()
                .setValue(uuid.toString())
                .build();
    }

    public static UUID toOriginalyUuid(Types.UUID uuid) {
        if(uuid == null) return null;
        return UUID.fromString(uuid.getValue());
    }
}
