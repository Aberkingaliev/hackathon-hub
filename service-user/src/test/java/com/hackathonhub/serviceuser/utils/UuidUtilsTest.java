package com.hackathonhub.serviceuser.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class UuidUtilsTest {
    @Test
    void stringToUUID_TestValid() {
        /*

        GIVEN

         */
        String uuidString = "99287713-4ed5-44dc-830f-4ed5b1d4e8be";
        UUID uuidOriginal = UUID.fromString(uuidString);

        /*

        EXECUTE

         */

        UUID uuidFromString = UuidUtils.stringToUUID(uuidString);

        /*

        ASSERTIONS

         */
        Assertions.assertEquals(UUID.class, uuidFromString.getClass());
        Assertions.assertEquals(uuidOriginal, uuidFromString);
    }

    @Test
    void stringToUUID_TestInvalid() {
        /*

        GIVEN

         */
        String uuidString = "*-4ed5-44dc-830f-4ed5b1d4e8be";

        /*

        ASSERTIONS

         */
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> UuidUtils.stringToUUID(uuidString),
                "String is not UUID: " + uuidString
        );
    }

    @Test
    void uuidToString_TestValid() {
        /*

        GIVEN

         */
        UUID uuidOriginal = UUID.fromString("99287713-4ed5-44dc-830f-4ed5b1d4e8be");
        String stringUuid = "99287713-4ed5-44dc-830f-4ed5b1d4e8be";

        /*

        EXECUTE

         */

        String stringFromUuid = UuidUtils.uuidToString(uuidOriginal);

        /*

        ASSERTIONS

         */
        Assertions.assertEquals(String.class, stringFromUuid.getClass());
        Assertions.assertEquals(stringUuid, stringFromUuid);
    }

}
