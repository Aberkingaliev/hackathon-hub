package com.hackathonhub.serviceuser.repositories;

import com.hackathonhub.serviceuser.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
