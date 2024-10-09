package com.aram.erpcrud.modules.auth;

import com.aram.erpcrud.modules.auth.payload.*;

public interface AuthService {

    AccountCreationResponse createAccount(CreateAccountCommand command);

    AccountPublicDetails findAccountByEmail(String email);

    AccountPublicDetails findAccountById(Long id);

    UpdateAccountResponse updateAccount(UpdateAccountCommand command);

    void deleteAccountById(Long id);
}