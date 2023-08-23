package com.hackathonhub.serviceuser.services.__mocks__;

import com.hackathonhub.serviceuser.grpc.UserGrpcService;
import com.hackathonhub.serviceuser.models.Role;
import com.hackathonhub.serviceuser.models.RoleEnum;
import com.hackathonhub.serviceuser.utils.UuidUtils;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public abstract class UserMockTestBase {
    protected static final String userId = "bd5a95ea-b73a-4a71-8611-9409cf88bc42";
    protected static final UUID userUuid = UuidUtils.stringToUUID("bd5a95ea-b73a-4a71-8611-9409cf88bc42");
    protected static final String username = "user";
    protected static final String fullName = "userFullName";
    protected static final String email = "userEmail@gmail.com";
    protected static final String password = "userpassword";
    protected static final String teamId = "bd5a95ea-b73a-4a71-8611-9409cf88bc42";
    protected static final UUID teamUuid = UuidUtils.stringToUUID("bd5a95ea-b73a-4a71-8611-9409cf88bc42");
    protected static final Boolean isActive = false;
    protected static final List<UserGrpcService.UserRole> listRole =  List.of(
            UserGrpcService.UserRole
                    .newBuilder()
                    .setId("bd5a95ea-b73a-4a71-8611-9409cf88bc42")
                    .setRole(UserGrpcService.role_enum.ROLE_USER)
                    .build()
    );
    protected static final HashSet<Role> HashSetRole =  new HashSet<>(
            List.of(
                    new Role()
                            .setId(UuidUtils.stringToUUID("bd5a95ea-b73a-4a71-8611-9409cf88bc42"))
                            .setRole_name(RoleEnum.ROLE_USER)
            )
    );
}
