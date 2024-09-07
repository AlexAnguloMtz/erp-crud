package com.aram.erpcrud.auth;

import com.aram.erpcrud.auth.payload.AccountCreationResponse;
import com.aram.erpcrud.auth.payload.AccountPublicDetails;
import com.aram.erpcrud.auth.payload.CreateAccountCommand;

import java.util.List;

public interface AuthService {

    AccountCreationResponse createAccount(CreateAccountCommand command);

    List<AccountPublicDetails> findAccounts(List<String> ids);

    AccountPublicDetails findAccountByEmail(String email);

}