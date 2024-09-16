package com.aram.erpcrud.users.application.command;

import com.aram.erpcrud.auth.AuthService;
import com.aram.erpcrud.users.domain.PersonalDetailsRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DeleteUserByIdCommandHandler {

    private final AuthService authService;
    private final PersonalDetailsRepository personalDetailsRepository;

    public DeleteUserByIdCommandHandler(
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