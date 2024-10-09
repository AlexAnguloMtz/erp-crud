package com.aram.erpcrud.modules.stockmovements.application.clients;

import com.aram.erpcrud.modules.personaldetails.PersonalDetailsService;
import com.aram.erpcrud.modules.personaldetails.payload.PersonalNameDTO;
import org.springframework.stereotype.Component;

@Component
class SimplePersonalDetailsServiceClient implements PersonalDetailsServiceClient {

    private final PersonalDetailsService personalDetailsService;

    SimplePersonalDetailsServiceClient(PersonalDetailsService personalDetailsService) {
        this.personalDetailsService = personalDetailsService;
    }

    @Override
    public PersonalNameDTO getPersonalName(Long id) {
        return personalDetailsService.getPersonalNameByAccountId(id);
    }
}