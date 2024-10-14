package com.aram.erpcrud.modules.authorization;

import com.aram.erpcrud.modules.authorization.application.command.CreateAccount;
import com.aram.erpcrud.modules.authorization.application.command.DeleteAccountById;
import com.aram.erpcrud.modules.authorization.application.command.UpdateAccount;
import com.aram.erpcrud.modules.authorization.application.query.GetAccountByEmail;
import com.aram.erpcrud.modules.authorization.application.query.GetAccountById;
import com.aram.erpcrud.modules.authorization.payload.*;
import org.springframework.stereotype.Component;

@Component
public class SimpleAuthService implements AuthService {

    private final CreateAccount createAccountCommandHandler;
    private final GetAccountByEmail getAccountByEmailQueryHandler;
    private final GetAccountById getAccountByIdQueryHandler;
    private final UpdateAccount updateAccountCommandHandler;
    private final DeleteAccountById deleteAccountByIdHandler;

    public SimpleAuthService(
            CreateAccount createAccountCommandHandler,
            GetAccountByEmail getAccountByEmailQueryHandler,
            GetAccountById getAccountByIdQueryHandler,
            UpdateAccount updateAccountCommandHandler,
            DeleteAccountById deleteAccountByIdHandler
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