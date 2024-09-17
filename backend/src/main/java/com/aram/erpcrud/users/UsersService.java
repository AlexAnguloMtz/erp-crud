package com.aram.erpcrud.users;

import com.aram.erpcrud.users.payload.PersonalNameDTO;

import java.util.UUID;

public interface UsersService {

    PersonalNameDTO getPersonalNameByAccountId(UUID accountId);

}