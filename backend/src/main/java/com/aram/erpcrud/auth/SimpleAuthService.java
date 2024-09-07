package com.aram.erpcrud.auth;

import com.aram.erpcrud.auth.application.CreateAccountCommandHandler;
import com.aram.erpcrud.auth.application.GetAccountByEmailQueryHandler;
import com.aram.erpcrud.auth.application.GetAccountsQueryHandler;
import com.aram.erpcrud.auth.payload.AccountCreationResponse;
import com.aram.erpcrud.auth.payload.AccountPublicDetails;
import com.aram.erpcrud.auth.payload.CreateAccountCommand;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SimpleAuthService implements AuthService {

    private final CreateAccountCommandHandler createAccountCommandHandler;
    private final GetAccountsQueryHandler getAccountsQueryHandler;
    private final GetAccountByEmailQueryHandler getAccountByEmailQueryHandler;

    public SimpleAuthService(CreateAccountCommandHandler createAccountCommandHandler, GetAccountsQueryHandler getAccountsQueryHandler, GetAccountByEmailQueryHandler getAccountByEmailQueryHandler) {
        this.createAccountCommandHandler = createAccountCommandHandler;
        this.getAccountsQueryHandler = getAccountsQueryHandler;
        this.getAccountByEmailQueryHandler = getAccountByEmailQueryHandler;
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
}