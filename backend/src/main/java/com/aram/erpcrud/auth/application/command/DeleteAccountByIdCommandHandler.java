package com.aram.erpcrud.auth.application.command;

import com.aram.erpcrud.auth.domain.AuthUserRepository;
import org.springframework.stereotype.Component;

@Component
public class DeleteAccountByIdCommandHandler {

    private final AuthUserRepository authUserRepository;

    public DeleteAccountByIdCommandHandler(AuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }

    public void handle(String id) {
        this.authUserRepository.deleteById(id);
    }
}