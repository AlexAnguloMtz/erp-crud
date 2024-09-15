package com.aram.erpcrud.movements.application.clients;

import com.aram.erpcrud.personaldetails.UsersService;
import com.aram.erpcrud.personaldetails.payload.PersonalNameDTO;
import org.springframework.stereotype.Component;

@Component
class SimpleUsersServiceClient implements UsersServiceClient {

    private final UsersService usersService;

    SimpleUsersServiceClient(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public PersonalNameDTO getResponsible(String responsibleId) {
        return usersService.getPersonalNameByAccountId(responsibleId);
    }
}