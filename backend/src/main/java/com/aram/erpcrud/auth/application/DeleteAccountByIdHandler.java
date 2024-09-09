package com.aram.erpcrud.auth.application;

import com.aram.erpcrud.auth.domain.AuthUserRepository;
import org.springframework.stereotype.Component;

@Component
public class DeleteAccountByIdHandler {

    private final AuthUserRepository authUserRepository;

    public DeleteAccountByIdHandler(AuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }

    public void handle(String id) {
        this.authUserRepository.deleteById(id);
    }
}