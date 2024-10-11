package com.aram.erpcrud.modules.authorization;

import com.aram.erpcrud.modules.authorization.payload.*;

public interface AuthService {

    AccountCreationResponse createAccount(CreateAccountCommand command);

    AccountPublicDetails findAccountByEmail(String email);

    AccountPublicDetails findAccountById(Long id);

    UpdateAccountResponse updateAccount(UpdateAccountCommand command);

    void deleteAccountById(Long id);
}