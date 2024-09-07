package com.aram.erpcrud.auth;

import com.aram.erpcrud.auth.application.CreateAccountCommandHandler;
import com.aram.erpcrud.auth.payload.AccountCreationResponse;
import com.aram.erpcrud.auth.payload.CreateAccountCommand;
import org.springframework.stereotype.Component;

@Component
public class SimpleAuthService implements AuthService {

    private final CreateAccountCommandHandler createAccountCommandHandler;

    public SimpleAuthService(CreateAccountCommandHandler createAccountCommandHandler) {
        this.createAccountCommandHandler = createAccountCommandHandler;
    }

    @Override
    public AccountCreationResponse createAccount(CreateAccountCommand command) {
        return createAccountCommandHandler.handle(command);
    }
}
