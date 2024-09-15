package com.aram.erpcrud.movements.application.clients;

import com.aram.erpcrud.personaldetails.payload.PersonalNameDTO;

public interface UsersServiceClient {

    PersonalNameDTO getResponsible(String responsibleId);

}