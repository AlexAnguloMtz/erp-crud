package com.aram.erpcrud.movements.application.clients;

import com.aram.erpcrud.users.UsersService;
import com.aram.erpcrud.users.payload.PersonalNameDTO;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
class SimplePersonalDetailsServiceClient implements PersonalDetailsServiceClient {

    private final UsersService usersService;

    SimplePersonalDetailsServiceClient(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public PersonalNameDTO getResponsible(UUID responsibleId) {
        return usersService.getPersonalNameByAccountId(responsibleId);
    }
}