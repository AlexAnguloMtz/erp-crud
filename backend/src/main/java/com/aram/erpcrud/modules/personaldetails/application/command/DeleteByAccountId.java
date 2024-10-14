package com.aram.erpcrud.modules.personaldetails.application.command;

import com.aram.erpcrud.modules.personaldetails.domain.PersonalDetailsRepository;
import org.springframework.stereotype.Component;

@Component
public class DeleteByAccountId {

    private final PersonalDetailsRepository personalDetailsRepository;

    public DeleteByAccountId(PersonalDetailsRepository personalDetailsRepository) {
        this.personalDetailsRepository = personalDetailsRepository;
    }

    public void handle(Long id) {
        personalDetailsRepository.deleteByAccountId(id);
    }

}
