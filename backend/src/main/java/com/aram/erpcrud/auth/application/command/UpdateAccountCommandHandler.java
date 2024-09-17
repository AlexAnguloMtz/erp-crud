package com.aram.erpcrud.auth.application.command;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

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

    @Transactional
    public UpdateAccountResponse handle(UpdateAccountCommand command) {
        Optional<AuthUser> authUserOptional = authUserRepository.findById(UUID.fromString(command.id()));
        if (authUserOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        AuthUser authUser = authUserOptional.get();

        Optional<AuthUser> authUserByEmailOptional = authUserRepository.findByEmail(command.email());
        if (authUserByEmailOptional.isPresent() && !authUser.getId().equals(authUserByEmailOptional.get().getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "email belongs to another user");
        }

        boolean changedSelf = command.requestingUserEmail().equals(authUserOptional.get().getEmail());

        Optional<AuthRole> authRoleOptional = authRoleRepository.findById(UUID.fromString(command.roleId()));
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
                savedAuthUser.getId().toString(),
                savedAuthUser.getEmail(),
                toRolePublicDetails(savedAuthUser.getRole()),
                token
        );
    }

    private RolePublicDetails toRolePublicDetails(AuthRole authRole) {
        return new RolePublicDetails(
                authRole.getId().toString(),
                authRole.getName()
        );
    }
}