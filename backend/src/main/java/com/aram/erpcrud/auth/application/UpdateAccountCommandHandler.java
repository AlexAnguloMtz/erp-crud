package com.aram.erpcrud.auth.application;

import com.aram.erpcrud.auth.domain.AuthRole;
import com.aram.erpcrud.auth.domain.AuthRoleRepository;
import com.aram.erpcrud.auth.domain.AuthUser;
import com.aram.erpcrud.auth.domain.AuthUserRepository;
import com.aram.erpcrud.auth.payload.UpdateAccountCommand;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class UpdateAccountCommandHandler {

    private final AuthRoleRepository authRoleRepository;
    private final AuthUserRepository authUserRepository;

    public UpdateAccountCommandHandler(
            AuthRoleRepository authService,
            AuthUserRepository authUserRepository
    ) {
        this.authRoleRepository = authService;
        this.authUserRepository = authUserRepository;
    }

    public void handle(UpdateAccountCommand command) {
        Optional<AuthUser> authUserOptional = authUserRepository.findById(command.id());
        if (authUserOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Optional<AuthRole> authRoleOptional = authRoleRepository.findById(command.roleId());
        if (authRoleOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        AuthUser authUser = authUserOptional.get();

        authUser.setRole(authRoleOptional.get());
        authUser.setEmail(command.email());

        authUserRepository.save(authUser);
    }
}