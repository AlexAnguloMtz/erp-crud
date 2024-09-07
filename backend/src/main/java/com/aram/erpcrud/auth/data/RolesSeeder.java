package com.aram.erpcrud.auth.data;

import com.aram.erpcrud.auth.domain.AuthRole;
import com.aram.erpcrud.auth.domain.AuthRoleRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Component
public class RolesSeeder {

    private final AuthRoleRepository authRoleRepository;

    public RolesSeeder(AuthRoleRepository authRoleRepository) {
        this.authRoleRepository = authRoleRepository;
    }

    public void seed() {
        if (authRoleRepository.count() == 0) {
            authRoleRepository.saveAll(allRoles());
        }
    }

    private Iterable<AuthRole> allRoles() {
        return allRolesNames().stream()
            .map(x -> new AuthRole(UUID.randomUUID().toString(), x))
            .toList();
    }

    private Collection<String> allRolesNames() {
        return Set.of(
            "ROLE_SUPER_USER",
            "ROLE_ADMIN",
            "ROLE_USER"
        );
    }
}