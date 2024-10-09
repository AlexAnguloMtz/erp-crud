package com.aram.erpcrud.modules.auth.application.query;

import com.aram.erpcrud.modules.auth.domain.AuthRole;
import com.aram.erpcrud.modules.auth.domain.Account;
import com.aram.erpcrud.modules.auth.domain.AccountRepository;
import com.aram.erpcrud.modules.auth.payload.AccountPublicDetails;
import com.aram.erpcrud.modules.auth.payload.RolePublicDetails;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class GetAccountByEmailQueryHandler {

    private final AccountRepository accountRepository;

    public GetAccountByEmailQueryHandler(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountPublicDetails handle(String email) {
        return  accountRepository.findByEmail(email)
            .map(this::toAccountPublicDetails)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private AccountPublicDetails toAccountPublicDetails(Account account) {
        return new AccountPublicDetails(
            account.getId(),
            account.getEmail(),
            toRolePublicDetails(account.getRole())
        );
    }

    private RolePublicDetails toRolePublicDetails(AuthRole role) {
        return new RolePublicDetails(
            role.getId(),
            role.getName()
        );
    }
}