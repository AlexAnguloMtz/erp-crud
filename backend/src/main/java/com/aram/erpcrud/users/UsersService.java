package com.aram.erpcrud.users;

import com.aram.erpcrud.users.payload.PersonalNameDTO;

public interface UsersService {

    PersonalNameDTO getPersonalNameByAccountId(String accountId);

}