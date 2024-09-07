package com.aram.erpcrud.auth;

import com.aram.erpcrud.auth.payload.AccountCreationResponse;
import com.aram.erpcrud.auth.payload.CreateAccountCommand;

public interface AuthService {
    AccountCreationResponse createAccount(CreateAccountCommand command);
}