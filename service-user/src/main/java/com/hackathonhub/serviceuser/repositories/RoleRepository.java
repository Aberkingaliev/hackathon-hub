package com.hackathonhub.serviceuser.repositories;

import com.hackathonhub.serviceuser.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
