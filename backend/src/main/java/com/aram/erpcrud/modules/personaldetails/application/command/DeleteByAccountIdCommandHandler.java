package com.aram.erpcrud.modules.personaldetails.application.command;

import com.aram.erpcrud.modules.personaldetails.domain.PersonalDetailsRepository;
import org.springframework.stereotype.Component;

@Component
public class DeleteByAccountIdCommandHandler {

    private final PersonalDetailsRepository personalDetailsRepository;

    public DeleteByAccountIdCommandHandler(PersonalDetailsRepository personalDetailsRepository) {
        this.personalDetailsRepository = personalDetailsRepository;
    }

    public void handle(Long id) {
        personalDetailsRepository.deleteByAccountId(id);
    }

}
