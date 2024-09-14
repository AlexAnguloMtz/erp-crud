package com.aram.erpcrud.users;

import com.aram.erpcrud.users.application.UserFacade;
import com.aram.erpcrud.users.payload.PersonalNameDTO;
import org.springframework.stereotype.Component;

@Component
class SimpleUsersService implements UsersService {

    private final UserFacade userFacade;

    SimpleUsersService(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @Override
    public PersonalNameDTO getPersonalNameByAccountId(String accountId) {
        return userFacade.getPersonalNameByAccountId(accountId);
    }
}