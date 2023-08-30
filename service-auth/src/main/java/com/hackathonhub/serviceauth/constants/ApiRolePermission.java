package com.hackathonhub.serviceauth.constants;

import com.hackathonhub.serviceauth.models.RoleEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Slf4j
public class ApiRolePermission {
    private static final HashMap<String, Set<RoleEnum>> API_ROLE_PERMISSION =
            new HashMap<>(
                    Map.ofEntries(
                            Map.entry("/service-user/api/role/create", Set.of(RoleEnum.ROLE_USER))
                    )
            );

    public static boolean isRoleAllowed(String route, Set<RoleEnum> role) {
        Set<RoleEnum> result = API_ROLE_PERMISSION.get(route);

        if (result == null) {
            log.error("Route {} is not found in API_ROLE_PERMISSION", route);
            return false;
        }

        return result.stream().anyMatch(role::contains);
    }
}
