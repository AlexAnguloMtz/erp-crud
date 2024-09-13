package com.aram.erpcrud.users.application.query;

import com.aram.erpcrud.auth.AuthService;
import com.aram.erpcrud.users.domain.PersonalDetailsRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class DeleteUserByIdQueryHandler {

    private final AuthService authService;
    private final PersonalDetailsRepository personalDetailsRepository;

    public DeleteUserByIdQueryHandler(
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