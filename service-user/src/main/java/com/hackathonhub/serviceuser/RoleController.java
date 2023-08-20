package com.hackathonhub.serviceuser;


import com.hackathonhub.serviceuser.models.Role;
import com.hackathonhub.serviceuser.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {

    @Autowired
    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    private RoleRepository roleRepository;

    @PostMapping("/role/create")
    public Role createRole(@RequestBody Role role) {
        System.out
                .println(role);
        return roleRepository.save(new Role().setRole_name(role.getRole_name()));
    }
}
