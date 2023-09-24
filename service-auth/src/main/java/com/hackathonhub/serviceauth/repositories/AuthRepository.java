package com.hackathonhub.serviceauth.repositories;

import com.hackathonhub.serviceauth.models.AuthToken;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface AuthRepository extends JpaRepository<AuthToken, UUID> {

    @Query("SELECT a FROM AuthToken a WHERE a.refreshToken = :refreshToken")
    Optional<AuthToken> findByRefreshToken(String refreshToken);


}
