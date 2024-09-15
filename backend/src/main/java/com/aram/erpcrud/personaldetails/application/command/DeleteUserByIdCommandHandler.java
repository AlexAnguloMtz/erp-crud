package com.aram.erpcrud.personaldetails.application.command;

import com.aram.erpcrud.auth.AuthService;
import com.aram.erpcrud.personaldetails.domain.PersonalDetailsRepository;
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