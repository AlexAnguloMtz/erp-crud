package com.aram.erpcrud.auth;

import com.aram.erpcrud.auth.application.*;
import com.aram.erpcrud.auth.payload.AccountCreationResponse;
import com.aram.erpcrud.auth.payload.AccountPublicDetails;
import com.aram.erpcrud.auth.payload.CreateAccountCommand;
import com.aram.erpcrud.auth.payload.UpdateAccountCommand;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SimpleAuthService implements AuthService {

    private final CreateAccountCommandHandler createAccountCommandHandler;
    private final GetAccountsQueryHandler getAccountsQueryHandler;
    private final GetAccountByEmailQueryHandler getAccountByEmailQueryHandler;
    private final GetAccountByIdQueryHandler getAccountByIdQueryHandler;
    private final UpdateAccountCommandHandler updateAccountCommandHandler;

    public SimpleAuthService(
            CreateAccountCommandHandler createAccountCommandHandler,
            GetAccountsQueryHandler getAccountsQueryHandler,
            GetAccountByEmailQueryHandler getAccountByEmailQueryHandler,
            GetAccountByIdQueryHandler getAccountByIdQueryHandler,
            UpdateAccountCommandHandler updateAccountCommandHandler
    ) {
        this.createAccountCommandHandler = createAccountCommandHandler;
        this.getAccountsQueryHandler = getAccountsQueryHandler;
        this.getAccountByEmailQueryHandler = getAccountByEmailQueryHandler;
        this.getAccountByIdQueryHandler = getAccountByIdQueryHandler;
        this.updateAccountCommandHandler = updateAccountCommandHandler;
    }

    @Override
    public AccountCreationResponse createAccount(CreateAccountCommand command) {
        return createAccountCommandHandler.handle(command);
    }

    @Override
    public List<AccountPublicDetails> findAccounts(List<String> accountIds) {
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
    public AccountPublicDetails updateAccount(UpdateAccountCommand command) {
        return updateAccountCommandHandler.handle(command);
    }
}