package com.aram.erpcrud.modules.authorization.application.command;

import com.aram.erpcrud.modules.authorization.util.JwtHandler;
import com.aram.erpcrud.modules.authorization.domain.Role;
import com.aram.erpcrud.modules.authorization.domain.RoleRepository;
import com.aram.erpcrud.modules.authorization.domain.Account;
import com.aram.erpcrud.modules.authorization.domain.AccountRepository;
import com.aram.erpcrud.modules.authorization.payload.RolePublicDetails;
import com.aram.erpcrud.modules.authorization.payload.UpdateAccountCommand;
import com.aram.erpcrud.modules.authorization.payload.UpdateAccountResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class UpdateAccount {

    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final JwtHandler jwtHandler;

    public UpdateAccount(
            RoleRepository authService,
            AccountRepository accountRepository, JwtHandler jwtHandler
    ) {
        this.roleRepository = authService;
        this.accountRepository = accountRepository;
        this.jwtHandler = jwtHandler;
    }

    @Transactional
    public UpdateAccountResponse handle(UpdateAccountCommand command) {
        Optional<Account> accountOptional = accountRepository.findById(command.id());
        if (accountOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Account account = accountOptional.get();

        Optional<Account> accountByEmailOptional = accountRepository.findByEmail(command.email());
        if (accountByEmailOptional.isPresent() && !account.getId().equals(accountByEmailOptional.get().getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "email belongs to another user");
        }

        boolean changedSelf = command.requestingUserEmail().equals(accountOptional.get().getEmail());

        Optional<Role> roleOptional = roleRepository.findById(command.roleId());
        if (roleOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        account.setRole(roleOptional.get());
        account.setEmail(command.email());

        Account savedAccount = accountRepository.save(account);

        String accessToken = null;
        if (changedSelf) {
            accessToken = jwtHandler.createToken(
                    savedAccount.getEmail(),
                    savedAccount.getRole().getCanonicalName()
            );
        }

        return new UpdateAccountResponse(
                savedAccount.getEmail(),
                toRolePublicDetails(savedAccount.getRole()),
                accessToken
        );
    }

    private RolePublicDetails toRolePublicDetails(Role role) {
        return new RolePublicDetails(
                role.getId(),
                role.getName()
        );
    }
}