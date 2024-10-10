package com.aram.erpcrud.modules.useraggregator.application.command;

import com.aram.erpcrud.modules.auth.AuthService;
import com.aram.erpcrud.modules.personaldetails.PersonalDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DeleteUserByIdCommandHandler {

    private final AuthService authService;
    private final PersonalDetailsService personalDetailsService;

    public DeleteUserByIdCommandHandler(
            AuthService authService,
            PersonalDetailsService personalDetailsService
    ) {
        this.authService = authService;
        this.personalDetailsService = personalDetailsService;
    }

    @Transactional
    public void handle(Long id) {
        personalDetailsService.deleteByAccountId(id);
        authService.deleteAccountById(id);
    }
}