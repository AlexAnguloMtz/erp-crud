package com.aram.erpcrud.modules.auth.application.command;

import com.aram.erpcrud.modules.auth.util.JwtHandler;
import com.aram.erpcrud.modules.auth.domain.AuthRole;
import com.aram.erpcrud.modules.auth.domain.AuthRoleRepository;
import com.aram.erpcrud.modules.auth.domain.Account;
import com.aram.erpcrud.modules.auth.domain.AccountRepository;
import com.aram.erpcrud.modules.auth.payload.RolePublicDetails;
import com.aram.erpcrud.modules.auth.payload.UpdateAccountCommand;
import com.aram.erpcrud.modules.auth.payload.UpdateAccountResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class UpdateAccountCommandHandler {

    private final AuthRoleRepository authRoleRepository;
    private final AccountRepository accountRepository;
    private final JwtHandler jwtHandler;

    public UpdateAccountCommandHandler(
            AuthRoleRepository authService,
            AccountRepository accountRepository, JwtHandler jwtHandler
    ) {
        this.authRoleRepository = authService;
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

        Optional<AuthRole> authRoleOptional = authRoleRepository.findById(command.roleId());
        if (authRoleOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        account.setRole(authRoleOptional.get());
        account.setEmail(command.email());

        Account savedAccount = accountRepository.save(account);

        String token = null;
        if (changedSelf) {
            token = jwtHandler.createToken(savedAccount.getEmail(), savedAccount.getRole().getCanonicalName());
        }

        return new UpdateAccountResponse(
                savedAccount.getId().toString(),
                savedAccount.getEmail(),
                toRolePublicDetails(savedAccount.getRole()),
                token
        );
    }

    private RolePublicDetails toRolePublicDetails(AuthRole authRole) {
        return new RolePublicDetails(
                authRole.getId(),
                authRole.getName()
        );
    }
}