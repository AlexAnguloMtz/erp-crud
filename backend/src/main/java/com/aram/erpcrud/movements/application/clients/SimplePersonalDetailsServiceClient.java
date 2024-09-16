package com.aram.erpcrud.movements.application.clients;

import com.aram.erpcrud.users.UsersService;
import com.aram.erpcrud.users.payload.PersonalNameDTO;
import org.springframework.stereotype.Component;

@Component
class SimplePersonalDetailsServiceClient implements UsersServiceClient {

    private final UsersService usersService;

    SimplePersonalDetailsServiceClient(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public PersonalNameDTO getResponsible(String responsibleId) {
        return usersService.getPersonalNameByAccountId(responsibleId);
    }
}