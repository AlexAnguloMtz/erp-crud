package com.aram.erpcrud.auth.application.command;

import com.aram.erpcrud.auth.domain.AuthUserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class DeleteAccountByIdCommandHandler {

    private final AuthUserRepository authUserRepository;

    public DeleteAccountByIdCommandHandler(AuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }

    @Transactional
    public void handle(String id) {
        this.authUserRepository.deleteById(UUID.fromString(id));
    }
}