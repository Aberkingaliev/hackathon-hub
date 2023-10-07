package com.hackathonhub.serviceidentity.constants;

import com.hackathonhub.serviceidentity.models.RoleEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * [Temporary solution] :)
 * A utility class that maps API routes to specific roles, allowing for role-based access control.
 * This class acts as a storage for role-route validation.
 */
@Slf4j
public class ApiRolePermission {

    /**
     * Static map containing the mapping of API routes to permitted roles.
     * This acts as an in-memory store for these mappings.
     */
    private static final HashMap<String, Set<RoleEnum>> API_ROLE_PERMISSION =
            new HashMap<>(
                    Map.ofEntries(
                            Map.entry("/service-user/api/role", Set.of(RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_USER)),
                            Map.entry("/service-user/api/role/update", Set.of(RoleEnum.ROLE_USER)),
                            Map.entry("/service-user/api/role/delete", Set.of(RoleEnum.ROLE_USER)),
                            Map.entry("/service-user/api/role/get", Set.of(RoleEnum.ROLE_USER))
                    )
            );

    /**
     * Verifies if a given role set is allowed to access a specific API route.
     *
     * @param route The API route in question.
     * @param role A set of roles associated with a user.
     * @return {@code true} if at least one role in the set is allowed to access the route, {@code false} otherwise.
     */
    public static boolean isRoleAllowed(String route, Set<RoleEnum> role) {
        Set<RoleEnum> result = API_ROLE_PERMISSION.get(route);

        if (result == null || result.isEmpty()) {
            log.info("Route {} is not found in API_ROLE_PERMISSION", route);
            return false;
        }

        return result.stream().anyMatch(role::contains);
    }
}
