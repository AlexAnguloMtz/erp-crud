package com.aram.erpcrud.auth.application.query;

import com.aram.erpcrud.auth.domain.AuthRole;
import com.aram.erpcrud.auth.domain.AuthUser;
import com.aram.erpcrud.auth.domain.AuthUserRepository;
import com.aram.erpcrud.auth.payload.AccountPublicDetails;
import com.aram.erpcrud.auth.payload.RolePublicDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetAccountsQueryHandler {

    private final AuthUserRepository authUserRepository;

    public GetAccountsQueryHandler(AuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }

    public List<AccountPublicDetails> handle(List<String> ids) {
        return authUserRepository.findAllById(ids).stream()
            .map(this::toAccountPublicDetails)
            .toList();
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