package com.aram.erpcrud.auth.application;

import com.aram.erpcrud.auth.domain.AuthUser;
import com.aram.erpcrud.auth.domain.AuthUserRepository;
import com.aram.erpcrud.auth.payload.AccountPublicDetails;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class GetAccountByEmailQueryHandler {

    private final AuthUserRepository authUserRepository;

    public GetAccountByEmailQueryHandler(AuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }

    public AccountPublicDetails handle(String email) {
        return  authUserRepository.findByEmail(email)
            .map(this::toAccountPublicDetails)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private AccountPublicDetails toAccountPublicDetails(AuthUser authUser) {
        return new AccountPublicDetails(
            authUser.getId(),
            authUser.getEmail(),
            authUser.getRole().getDescription()
        );
    }
}