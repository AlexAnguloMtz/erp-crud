package com.aram.erpcrud.users.application;

import com.aram.erpcrud.auth.AuthService;
import com.aram.erpcrud.users.domain.PersonalDetailsRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class DeleteUserByIdHandler {

    private final AuthService authService;
    private final PersonalDetailsRepository personalDetailsRepository;

    public DeleteUserByIdHandler(
            AuthService authService,
            PersonalDetailsRepository personalDetailsRepository
    ) {
        this.authService = authService;
        this.personalDetailsRepository = personalDetailsRepository;
    }

    @Transactional
    public void handle(String id) {
        personalDetailsRepository.deleteByAccountId(id);
        authService.deleteAccountById(id);
    }
}