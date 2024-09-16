package com.aram.erpcrud.auth;

import com.aram.erpcrud.auth.payload.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AuthService {

    AccountCreationResponse createAccount(CreateAccountCommand command);

    List<AccountPublicDetails> findAccounts(Set<String> ids);

    AccountPublicDetails findAccountByEmail(String email);

    Optional<AccountPublicDetails> findAccountById(String id);

    UpdateAccountResponse updateAccount(UpdateAccountCommand command);

    void deleteAccountById(String id);
}