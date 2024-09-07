package com.aram.erpcrud.auth.data;

import com.aram.erpcrud.auth.domain.AuthRole;
import com.aram.erpcrud.auth.domain.AuthRoleRepository;
import com.aram.erpcrud.auth.domain.AuthUser;
import com.aram.erpcrud.auth.domain.AuthUserRepository;
import com.aram.erpcrud.users.domain.PersonalDetails;
import com.aram.erpcrud.users.domain.PersonalDetailsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class RootUserSeeder {

    private final String rootUserEmail;
    private final String rootUserPassword;
    private final PasswordEncoder passwordEncoder;
    private final AuthUserRepository authUserRepository;
    private final AuthRoleRepository authRoleRepository;
    private final PersonalDetailsRepository personalDetailsRepository;

    public RootUserSeeder(
            @Value("${config.root-user.email}") String rootUserEmail,
            @Value("${config.root-user.password}") String rootUserPassword,
            PasswordEncoder passwordEncoder,
            AuthUserRepository authUserRepository,
            AuthRoleRepository authRoleRepository,
            PersonalDetailsRepository personalDetailsRepository
    ) {
        this.rootUserEmail = rootUserEmail;
        this.rootUserPassword = rootUserPassword;
        this.passwordEncoder = passwordEncoder;
        this.authUserRepository = authUserRepository;
        this.authRoleRepository = authRoleRepository;
        this.personalDetailsRepository = personalDetailsRepository;
    }

    public void seed() {
        if (authUserRepository.count() == 0) {
            AuthUser rootUserAccount = authUserRepository.save(rootUserAccount());

            PersonalDetails rootUserPersonalDetails = createRootUserPersonalDetails(
                UUID.randomUUID().toString(),
                rootUserAccount.getId()
            );

            personalDetailsRepository.save(rootUserPersonalDetails);
        }
    }

    private AuthUser rootUserAccount() {
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

    private PersonalDetails createRootUserPersonalDetails(String id, String accountId) {
        return new PersonalDetails(
                id,
                accountId,
                "Usuario Raiz",
                "Apellido",
                "Estado",
                "Ciudad",
                "Colonia",
                "0",
                "0000000000",
                "00000"
        );
    }
}