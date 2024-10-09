package com.aram.erpcrud.modules.personaldetails;

import com.aram.erpcrud.modules.personaldetails.payload.CreatePersonalDetailsCommand;
import com.aram.erpcrud.modules.personaldetails.payload.PersonalDetailsDTO;
import com.aram.erpcrud.modules.personaldetails.payload.PersonalNameDTO;
import com.aram.erpcrud.modules.personaldetails.payload.UpdatePersonalDetailsCommand;

public interface PersonalDetailsService {

    PersonalNameDTO getPersonalNameByAccountId(Long id);

    void createPersonalDetails(CreatePersonalDetailsCommand command);

    void deleteByAccountId(Long id);

    PersonalDetailsDTO updatePersonalDetails(UpdatePersonalDetailsCommand command);
}