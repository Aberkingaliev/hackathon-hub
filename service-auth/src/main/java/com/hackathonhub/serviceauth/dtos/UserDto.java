package com.hackathonhub.serviceauth.dtos;

import com.hackathonhub.serviceauth.models.Role;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Data
@Accessors(chain = true)
public class UserDto implements Serializable {

    private UUID id;
    private String username;
    private String fullName;
    private String email;
    private Boolean isActivated;
    private Set<Role> roles = new HashSet<>();
}
