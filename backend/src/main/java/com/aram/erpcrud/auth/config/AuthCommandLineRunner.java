package com.aram.erpcrud.auth.config;

import com.aram.erpcrud.auth.domain.AuthRole;
import com.aram.erpcrud.auth.domain.AuthUser;
import com.aram.erpcrud.auth.domain.AuthUserRepository;
import com.aram.erpcrud.auth.domain.AuthRoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
public class AuthCommandLineRunner implements CommandLineRunner {

    private final String rootUserEmail;
    private final String rootUserPassword;
    private final PasswordEncoder passwordEncoder;
    private final AuthUserRepository authUserRepository;
    private final AuthRoleRepository authRoleRepository;

    public AuthCommandLineRunner(
            @Value("${config.root-user.email}") String rootUserEmail,
            @Value("${config.root-user.password}") String rootUserPassword,
            PasswordEncoder passwordEncoder,
            AuthUserRepository authUserRepository,
            AuthRoleRepository roleRepository
    ) {
        this.rootUserEmail = rootUserEmail;
        this.rootUserPassword = rootUserPassword;
        this.passwordEncoder = passwordEncoder;
        this.authUserRepository = authUserRepository;
        this.authRoleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            if (authRoleRepository.count() == 0) {
                authRoleRepository.saveAll(allRoles());
            }

            if (authUserRepository.count() == 0) {
                authUserRepository.save(rootUser());
            }
        } catch (Exception exception) {
            throw new RuntimeException("could not create roles and root user", exception);
        }
    }

    private Iterable<AuthRole> allRoles() {
        return Set.of(
                new AuthRole(UUID.randomUUID().toString(), "ROLE_SUPER_USER"),
                new AuthRole(UUID.randomUUID().toString(), "ROLE_ADMIN"),
                new AuthRole(UUID.randomUUID().toString(), "ROLE_USER")
        );
    }

    private AuthUser rootUser() {
        Optional<AuthRole> superUserRoleOptional = authRoleRepository.findByDescription("ROLE_SUPER_USER");
        if (superUserRoleOptional.isEmpty()) {
            throw new RuntimeException("Could not find root user role");
        }

        return new AuthUser(
                UUID.randomUUID().toString(),
                superUserRoleOptional.get(),
                rootUserEmail,
                passwordEncoder.encode(rootUserPassword)
        );
    }
}