package com.aram.erpcrud.modules.authorization;

import com.aram.erpcrud.modules.authorization.application.command.CreateAccountCommandHandler;
import com.aram.erpcrud.modules.authorization.application.command.DeleteAccountByIdCommandHandler;
import com.aram.erpcrud.modules.authorization.application.command.UpdateAccountCommandHandler;
import com.aram.erpcrud.modules.authorization.application.query.GetAccountByEmailQueryHandler;
import com.aram.erpcrud.modules.authorization.application.query.GetAccountByIdQueryHandler;
import com.aram.erpcrud.modules.authorization.payload.*;
import org.springframework.stereotype.Component;

@Component
public class SimpleAuthService implements AuthService {

    private final CreateAccountCommandHandler createAccountCommandHandler;
    private final GetAccountByEmailQueryHandler getAccountByEmailQueryHandler;
    private final GetAccountByIdQueryHandler getAccountByIdQueryHandler;
    private final UpdateAccountCommandHandler updateAccountCommandHandler;
    private final DeleteAccountByIdCommandHandler deleteAccountByIdHandler;

    public SimpleAuthService(
            CreateAccountCommandHandler createAccountCommandHandler,
            GetAccountByEmailQueryHandler getAccountByEmailQueryHandler,
            GetAccountByIdQueryHandler getAccountByIdQueryHandler,
            UpdateAccountCommandHandler updateAccountCommandHandler,
            DeleteAccountByIdCommandHandler deleteAccountByIdHandler
    ) {
        this.createAccountCommandHandler = createAccountCommandHandler;
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
    public AccountPublicDetails findAccountByEmail(String email) {
        return getAccountByEmailQueryHandler.handle(email);
    }

    @Override
    public AccountPublicDetails findAccountById(Long id) {
        return getAccountByIdQueryHandler.handle(id);
    }

    @Override
    public UpdateAccountResponse updateAccount(UpdateAccountCommand command) {
        return updateAccountCommandHandler.handle(command);
    }

    @Override
    public void deleteAccountById(Long id) {
        deleteAccountByIdHandler.handle(id);
    }
}