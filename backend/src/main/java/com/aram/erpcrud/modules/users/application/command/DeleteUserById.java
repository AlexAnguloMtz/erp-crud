package com.aram.erpcrud.modules.users.application.command;

import com.aram.erpcrud.modules.authorization.AuthService;
import com.aram.erpcrud.modules.personaldetails.PersonalDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DeleteUserById {

    private final AuthService authService;
    private final PersonalDetailsService personalDetailsService;

    public DeleteUserById(
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