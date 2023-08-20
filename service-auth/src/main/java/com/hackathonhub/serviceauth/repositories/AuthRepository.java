package com.hackathonhub.serviceauth.repositories;

import com.hackathonhub.serviceauth.models.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface AuthRepository extends JpaRepository<AuthToken, UUID> {

    AuthToken findByRefreshToken(String refreshToken);

}