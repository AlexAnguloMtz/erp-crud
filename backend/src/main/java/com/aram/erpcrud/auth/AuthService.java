package com.aram.erpcrud.auth;

import com.aram.erpcrud.auth.payload.AccountCreationResponse;
import com.aram.erpcrud.auth.payload.AccountPublicDetails;
import com.aram.erpcrud.auth.payload.CreateAccountCommand;
import com.aram.erpcrud.auth.payload.UpdateAccountCommand;

import java.util.List;
import java.util.Optional;

public interface AuthService {

    AccountCreationResponse createAccount(CreateAccountCommand command);

    List<AccountPublicDetails> findAccounts(List<String> ids);

    AccountPublicDetails findAccountByEmail(String email);

    Optional<AccountPublicDetails> findAccountById(String id);

    void updateAccount(UpdateAccountCommand command);
}