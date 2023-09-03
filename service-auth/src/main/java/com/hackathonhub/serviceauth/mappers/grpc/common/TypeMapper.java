package com.hackathonhub.serviceauth.mappers.grpc.common;

import com.google.protobuf.Timestamp;
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

    public static Timestamp toGrpcTimestamp(java.sql.Timestamp timestamp) {
        return Timestamp.newBuilder()
                .setSeconds(timestamp.getNanos())
                .build();
    }

    public static Long toOriginalyTimestamp(Timestamp timestamp) {
        return timestamp.getSeconds();
    }
}
