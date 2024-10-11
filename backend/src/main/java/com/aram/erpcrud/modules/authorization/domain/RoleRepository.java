package com.aram.erpcrud.modules.authorization.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByCanonicalName(String canonicalName);
}
