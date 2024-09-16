package com.aram.erpcrud.auth.application.query;

import com.aram.erpcrud.auth.domain.AuthRole;
import com.aram.erpcrud.auth.domain.AuthUser;
import com.aram.erpcrud.auth.domain.AuthUserRepository;
import com.aram.erpcrud.auth.payload.AccountPublicDetails;
import com.aram.erpcrud.auth.payload.RolePublicDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class GetAccountsByIdsQueryHandler {

    private final AuthUserRepository authUserRepository;

    public GetAccountsByIdsQueryHandler(AuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }

    public List<AccountPublicDetails> handle(Set<String> ids) {
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