package com.hackathonhub.serviceidentity.mappers.grpc.common;

import com.hackathonhub.common.grpc.Types;

import java.sql.Timestamp;
import java.util.UUID;

public class TypeMapper {


    public static Types.UUID toGrpcUuid(UUID uuid) {
        if(uuid == null) return null;
        return Types.UUID
                .newBuilder()
                .setValue(uuid.toString())
                .build();
    }

    public static UUID toOriginallyUuid(Types.UUID uuid) {
        return uuid == null ? null : UUID.fromString(uuid.getValue());
    }

    public static Long toOriginallyTimestamp(com.google.protobuf.Timestamp timestamp) {
        return timestamp == null ? null : timestamp.getSeconds();
    }

    public static com.google.protobuf.Timestamp toGrpcTimestamp(Timestamp timestamp) {
        if(timestamp == null) return null;
        return com.google.protobuf.Timestamp
                .newBuilder()
                .setSeconds(timestamp.getTime())
                .build();
    }
}
