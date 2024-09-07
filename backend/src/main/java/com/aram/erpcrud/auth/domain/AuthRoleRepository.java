package com.aram.erpcrud.auth.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRoleRepository extends JpaRepository<AuthRole, String> {
    Optional<AuthRole> findByDescription(String roleSuperUser);
}
