package com.aram.erpcrud.modules.personaldetails;

import com.aram.erpcrud.modules.personaldetails.application.PersonalDetailsFacade;
import com.aram.erpcrud.modules.personaldetails.payload.CreatePersonalDetailsCommand;
import com.aram.erpcrud.modules.personaldetails.payload.PersonalDetailsDTO;
import com.aram.erpcrud.modules.personaldetails.payload.PersonalNameDTO;
import com.aram.erpcrud.modules.personaldetails.payload.UpdatePersonalDetailsCommand;
import org.springframework.stereotype.Component;

@Component
public class SimplePersonalDetailsService implements PersonalDetailsService {

    private final PersonalDetailsFacade personalDetailsFacade;

    public SimplePersonalDetailsService(PersonalDetailsFacade personalDetailsFacade) {
        this.personalDetailsFacade = personalDetailsFacade;
    }

    @Override
    public PersonalNameDTO getPersonalNameByAccountId(Long id) {
        return personalDetailsFacade.getPersonalNameByAccountId(id);
    }

    @Override
    public void createPersonalDetails(CreatePersonalDetailsCommand command) {
        personalDetailsFacade.createPersonalDetails(command);
    }

    @Override
    public void deleteByAccountId(Long id) {
        personalDetailsFacade.deleteByAccountId(id);
    }

    @Override
    public PersonalDetailsDTO updatePersonalDetails(UpdatePersonalDetailsCommand command) {
        return personalDetailsFacade.updatePersonalDetails(command);
    }
}
