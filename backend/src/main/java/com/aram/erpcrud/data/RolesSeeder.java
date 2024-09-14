package com.aram.erpcrud.data;

import com.aram.erpcrud.auth.domain.AuthRole;
import com.aram.erpcrud.auth.domain.AuthRoleRepository;
import org.springframework.stereotype.Component;

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
        return Set.of(
            new AuthRole(UUID.randomUUID().toString(), "ROLE_SUPER_USER", "Super Usuario"),
            new AuthRole(UUID.randomUUID().toString(), "ROLE_ADMIN", "Administrador"),
            new AuthRole(UUID.randomUUID().toString(), "ROLE_USER", "Usuario")
        );
    }
}