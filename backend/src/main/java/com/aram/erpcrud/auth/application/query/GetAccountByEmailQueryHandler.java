package com.aram.erpcrud.auth.application.query;

import com.aram.erpcrud.auth.domain.AuthRole;
import com.aram.erpcrud.auth.domain.AuthUser;
import com.aram.erpcrud.auth.domain.AuthUserRepository;
import com.aram.erpcrud.auth.payload.AccountPublicDetails;
import com.aram.erpcrud.auth.payload.RolePublicDetails;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class GetAccountByEmailQueryHandler {

    private final AuthUserRepository authUserRepository;

    public GetAccountByEmailQueryHandler(AuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }

    public AccountPublicDetails handle(String email) {
        return  authUserRepository.findByEmail(email)
            .map(this::toAccountPublicDetails)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private AccountPublicDetails toAccountPublicDetails(AuthUser authUser) {
        return new AccountPublicDetails(
            authUser.getId(),
            authUser.getEmail(),
            toRolePublicDetails(authUser.getRole())
        );
    }

    private RolePublicDetails toRolePublicDetails(AuthRole role) {
        return new RolePublicDetails(
            role.getId(),
            role.getName()
        );
    }
}