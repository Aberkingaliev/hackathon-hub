package com.hackathonhub.serviceuser.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;


@Slf4j
public class UuidUtils {

    public static UUID stringToUUID(String uuid) throws IllegalArgumentException {
        try {
            return UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            log.error("String is not UUID: {}\nError: {}", uuid, e.getMessage());
            throw new IllegalArgumentException("String is not UUID: " + uuid);
        }
    }

    public static String uuidToString(UUID uuid) throws IllegalArgumentException {
        try {
            return uuid.toString();
        } catch (IllegalArgumentException e) {
            log.error("UUID is not valid: {},\nError: {}", uuid, e.getMessage());
            throw new IllegalArgumentException("UUID is not valid: " + uuid);
        }
    }
}
