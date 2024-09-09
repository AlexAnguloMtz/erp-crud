package com.aram.erpcrud.auth.application;

import com.aram.erpcrud.auth.config.JwtHandler;
import com.aram.erpcrud.auth.domain.AuthRole;
import com.aram.erpcrud.auth.domain.AuthRoleRepository;
import com.aram.erpcrud.auth.domain.AuthUser;
import com.aram.erpcrud.auth.domain.AuthUserRepository;
import com.aram.erpcrud.auth.payload.RolePublicDetails;
import com.aram.erpcrud.auth.payload.UpdateAccountCommand;
import com.aram.erpcrud.auth.payload.UpdateAccountResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class UpdateAccountCommandHandler {

    private final AuthRoleRepository authRoleRepository;
    private final AuthUserRepository authUserRepository;
    private final JwtHandler jwtHandler;

    public UpdateAccountCommandHandler(
            AuthRoleRepository authService,
            AuthUserRepository authUserRepository, JwtHandler jwtHandler
    ) {
        this.authRoleRepository = authService;
        this.authUserRepository = authUserRepository;
        this.jwtHandler = jwtHandler;
    }

    public UpdateAccountResponse handle(UpdateAccountCommand command) {
        Optional<AuthUser> authUserOptional = authUserRepository.findById(command.id());
        if (authUserOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        AuthUser authUser = authUserOptional.get();
        boolean changedSelf = command.requestingUserEmail().equals(authUserOptional.get().getEmail());

        Optional<AuthRole> authRoleOptional = authRoleRepository.findById(command.roleId());
        if (authRoleOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        authUser.setRole(authRoleOptional.get());
        authUser.setEmail(command.email());

        AuthUser savedAuthUser = authUserRepository.save(authUser);

        String token = null;
        if (changedSelf) {
            token = jwtHandler.createToken(savedAuthUser.getEmail(), savedAuthUser.getRole().getCanonicalName());
        }

        return new UpdateAccountResponse(
                savedAuthUser.getId(),
                savedAuthUser.getEmail(),
                toRolePublicDetails(savedAuthUser.getRole()),
                token
        );
    }

    private RolePublicDetails toRolePublicDetails(AuthRole authRole) {
        return new RolePublicDetails(
                authRole.getId(),
                authRole.getName()
        );
    }
}