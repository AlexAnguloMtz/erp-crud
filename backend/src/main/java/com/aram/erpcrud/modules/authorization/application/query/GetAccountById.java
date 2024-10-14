package com.aram.erpcrud.modules.authorization.application.query;

import com.aram.erpcrud.modules.authorization.domain.Role;
import com.aram.erpcrud.modules.authorization.domain.Account;
import com.aram.erpcrud.modules.authorization.domain.AccountRepository;
import com.aram.erpcrud.modules.authorization.payload.AccountPublicDetails;
import com.aram.erpcrud.modules.authorization.payload.RolePublicDetails;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class GetAccountById {

    private final AccountRepository accountRepository;

    public GetAccountById(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountPublicDetails handle(Long id) {
        return accountRepository.findById(id)
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

    private RolePublicDetails toRolePublicDetails(Role role) {
        return new RolePublicDetails(role.getId(), role.getName());
    }

}
