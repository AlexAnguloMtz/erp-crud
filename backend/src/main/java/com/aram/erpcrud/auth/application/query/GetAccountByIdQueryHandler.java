package com.aram.erpcrud.auth.application.query;

import com.aram.erpcrud.auth.domain.AuthRole;
import com.aram.erpcrud.auth.domain.AuthUser;
import com.aram.erpcrud.auth.domain.AuthUserRepository;
import com.aram.erpcrud.auth.payload.AccountPublicDetails;
import com.aram.erpcrud.auth.payload.RolePublicDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class GetAccountByIdQueryHandler {

    private final AuthUserRepository authUserRepository;

    public GetAccountByIdQueryHandler(AuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }

    public Optional<AccountPublicDetails> handle(String id) {
        Optional<AuthUser> authUserOptional = authUserRepository.findById(UUID.fromString(id));
        return authUserOptional.map(this::toAccountPublicDetails);
    }

    private AccountPublicDetails toAccountPublicDetails(AuthUser authUser) {
        return new AccountPublicDetails(
            authUser.getId().toString(),
            authUser.getEmail(),
            toRolePublicDetails(authUser.getRole())
        );
    }

    private RolePublicDetails toRolePublicDetails(AuthRole authRole) {
        return new RolePublicDetails(authRole.getId().toString(), authRole.getName());
    }

}
