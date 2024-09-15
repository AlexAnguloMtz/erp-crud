package com.aram.erpcrud.personaldetails;

import com.aram.erpcrud.personaldetails.payload.PersonalNameDTO;

public interface UsersService {

    PersonalNameDTO getPersonalNameByAccountId(String accountId);

}