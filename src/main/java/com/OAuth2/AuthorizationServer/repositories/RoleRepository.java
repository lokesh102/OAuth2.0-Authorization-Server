package com.OAuth2.AuthorizationServer.repositories;

import com.OAuth2.AuthorizationServer.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
