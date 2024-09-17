package com.aram.erpcrud.movements.application.clients;

import com.aram.erpcrud.users.payload.PersonalNameDTO;

import java.util.UUID;

public interface PersonalDetailsServiceClient {

    PersonalNameDTO getResponsible(UUID responsibleId);

}