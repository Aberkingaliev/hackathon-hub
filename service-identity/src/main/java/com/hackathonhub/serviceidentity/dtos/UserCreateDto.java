package com.hackathonhub.serviceidentity.dtos;

import com.hackathonhub.serviceidentity.models.Role;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Data
public class UserCreateDto implements Serializable {


    private String username;
    private String fullName;
    private String email;
    private String password;
    private Boolean isActivated = false;
    private Set<Role> roles = new HashSet<>();

}
