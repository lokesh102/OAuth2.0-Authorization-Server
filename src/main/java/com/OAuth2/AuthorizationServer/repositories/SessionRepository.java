package com.OAuth2.AuthorizationServer.repositories;


import com.OAuth2.AuthorizationServer.models.Session;
import com.OAuth2.AuthorizationServer.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session,Long> {
    public Optional<Session> findByTokenAndUser(String token, User user);
}
