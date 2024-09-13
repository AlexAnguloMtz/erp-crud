package com.aram.erpcrud.auth.application.command;

import com.aram.erpcrud.auth.domain.AuthRole;
import com.aram.erpcrud.auth.domain.AuthRoleRepository;
import com.aram.erpcrud.auth.domain.AuthUser;
import com.aram.erpcrud.auth.domain.AuthUserRepository;
import com.aram.erpcrud.auth.payload.AccountCreationResponse;
import com.aram.erpcrud.auth.payload.CreateAccountCommand;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Component
public class CreateAccountCommandHandler {

    private final AuthUserRepository authUserRepository;
    private final AuthRoleRepository authRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateAccountCommandHandler(
            AuthUserRepository authUserRepository,
            AuthRoleRepository authRoleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.authUserRepository = authUserRepository;
        this.authRoleRepository = authRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AccountCreationResponse handle(CreateAccountCommand command) {
        Optional<AuthUser> userOptional = authUserRepository.findByEmail(command.email());
        if (userOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        Optional<AuthRole> roleOptional =  authRoleRepository.findById(command.roleId());
        if (roleOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        String encodedPassword = passwordEncoder.encode(command.password());

        AuthUser user = new AuthUser(
            UUID.randomUUID().toString(),
            roleOptional.get(),
            command.email(),
            encodedPassword
        );

        AuthUser authUser = authUserRepository.save(user);

        return new AccountCreationResponse(authUser.getId());
    }
}