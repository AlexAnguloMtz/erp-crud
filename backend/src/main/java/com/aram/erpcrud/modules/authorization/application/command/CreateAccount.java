package com.aram.erpcrud.modules.authorization.application.command;

import com.aram.erpcrud.modules.authorization.domain.Role;
import com.aram.erpcrud.modules.authorization.domain.RoleRepository;
import com.aram.erpcrud.modules.authorization.domain.Account;
import com.aram.erpcrud.modules.authorization.domain.AccountRepository;
import com.aram.erpcrud.modules.authorization.payload.AccountCreationResponse;
import com.aram.erpcrud.modules.authorization.payload.CreateAccountCommand;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class CreateAccount {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateAccount(
            AccountRepository accountRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AccountCreationResponse handle(CreateAccountCommand command) {
        Optional<Account> userOptional = accountRepository.findByEmail(command.email());
        if (userOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        Optional<Role> roleOptional = roleRepository.findById(command.roleId());
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