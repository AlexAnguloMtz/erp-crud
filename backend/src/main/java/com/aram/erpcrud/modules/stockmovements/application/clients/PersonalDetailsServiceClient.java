package com.aram.erpcrud.modules.stockmovements.application.clients;

import com.aram.erpcrud.modules.personaldetails.payload.PersonalNameDTO;

public interface PersonalDetailsServiceClient {

    PersonalNameDTO getPersonalName(Long id);

}