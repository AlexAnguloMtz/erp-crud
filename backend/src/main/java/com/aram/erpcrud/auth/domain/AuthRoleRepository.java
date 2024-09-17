package com.aram.erpcrud.auth.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthRoleRepository extends JpaRepository<AuthRole, UUID> {
    Optional<AuthRole> findByCanonicalName(String roleSuperUser);
}
