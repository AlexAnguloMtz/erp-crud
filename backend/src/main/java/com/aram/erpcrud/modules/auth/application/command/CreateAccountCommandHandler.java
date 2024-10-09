package com.aram.erpcrud.modules.auth.application.command;

import com.aram.erpcrud.modules.auth.domain.AuthRole;
import com.aram.erpcrud.modules.auth.domain.AuthRoleRepository;
import com.aram.erpcrud.modules.auth.domain.Account;
import com.aram.erpcrud.modules.auth.domain.AccountRepository;
import com.aram.erpcrud.modules.auth.payload.AccountCreationResponse;
import com.aram.erpcrud.modules.auth.payload.CreateAccountCommand;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class CreateAccountCommandHandler {

    private final AccountRepository accountRepository;
    private final AuthRoleRepository authRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateAccountCommandHandler(
            AccountRepository accountRepository,
            AuthRoleRepository authRoleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.accountRepository = accountRepository;
        this.authRoleRepository = authRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AccountCreationResponse handle(CreateAccountCommand command) {
        Optional<Account> userOptional = accountRepository.findByEmail(command.email());
        if (userOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        Optional<AuthRole> roleOptional = authRoleRepository.findById(command.roleId());
        if (roleOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        String encodedPassword = passwordEncoder.encode(command.password());

        Account account = new Account();
        account.setRole(roleOptional.get());
        account.setEmail(command.email());
        account.setPassword(encodedPassword);

        Account savedAccount = accountRepository.save(account);

        return new AccountCreationResponse(savedAccount.getId());
    }
}