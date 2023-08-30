package com.hackathonhub.serviceuser;


import com.hackathonhub.serviceuser.models.Role;
import com.hackathonhub.serviceuser.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/create")
    public Role createRole(@RequestBody Role role) {
        return roleRepository.save(
                new Role()
                        .setRole_name(role.getRole_name())
        );
    }
}
