package com.aram.erpcrud.auth;

import com.aram.erpcrud.auth.application.command.CreateAccountCommandHandler;
import com.aram.erpcrud.auth.application.command.DeleteAccountByIdCommandHandler;
import com.aram.erpcrud.auth.application.command.UpdateAccountCommandHandler;
import com.aram.erpcrud.auth.application.query.GetAccountByEmailQueryHandler;
import com.aram.erpcrud.auth.application.query.GetAccountByIdQueryHandler;
import com.aram.erpcrud.auth.application.query.GetAccountsByIdsQueryHandler;
import com.aram.erpcrud.auth.payload.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
public class SimpleAuthService implements AuthService {

    private final CreateAccountCommandHandler createAccountCommandHandler;
    private final GetAccountsByIdsQueryHandler getAccountsQueryHandler;
    private final GetAccountByEmailQueryHandler getAccountByEmailQueryHandler;
    private final GetAccountByIdQueryHandler getAccountByIdQueryHandler;
    private final UpdateAccountCommandHandler updateAccountCommandHandler;
    private final DeleteAccountByIdCommandHandler deleteAccountByIdHandler;

    public SimpleAuthService(
            CreateAccountCommandHandler createAccountCommandHandler,
            GetAccountsByIdsQueryHandler getAccountsQueryHandler,
            GetAccountByEmailQueryHandler getAccountByEmailQueryHandler,
            GetAccountByIdQueryHandler getAccountByIdQueryHandler,
            UpdateAccountCommandHandler updateAccountCommandHandler,
            DeleteAccountByIdCommandHandler deleteAccountByIdHandler
    ) {
        this.createAccountCommandHandler = createAccountCommandHandler;
        this.getAccountsQueryHandler = getAccountsQueryHandler;
        this.getAccountByEmailQueryHandler = getAccountByEmailQueryHandler;
        this.getAccountByIdQueryHandler = getAccountByIdQueryHandler;
        this.updateAccountCommandHandler = updateAccountCommandHandler;
        this.deleteAccountByIdHandler = deleteAccountByIdHandler;
    }

    @Override
    public AccountCreationResponse createAccount(CreateAccountCommand command) {
        return createAccountCommandHandler.handle(command);
    }

    @Override
    public List<AccountPublicDetails> findAccounts(Set<UUID> accountIds) {
        return getAccountsQueryHandler.handle(accountIds);
    }

    @Override
    public AccountPublicDetails findAccountByEmail(String email) {
        return getAccountByEmailQueryHandler.handle(email);
    }

    @Override
    public Optional<AccountPublicDetails> findAccountById(String id) {
        return getAccountByIdQueryHandler.handle(id);
    }

    @Override
    public UpdateAccountResponse updateAccount(UpdateAccountCommand command) {
        return updateAccountCommandHandler.handle(command);
    }

    @Override
    public void deleteAccountById(String id) {
        deleteAccountByIdHandler.handle(id);
    }
}