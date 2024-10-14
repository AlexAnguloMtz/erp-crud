package com.aram.erpcrud.modules.personaldetails.application;

import com.aram.erpcrud.modules.personaldetails.application.command.CreatePersonalDetails;
import com.aram.erpcrud.modules.personaldetails.application.command.DeleteByAccountId;
import com.aram.erpcrud.modules.personaldetails.application.command.UpdatePersonalDetails;
import com.aram.erpcrud.modules.personaldetails.application.query.GetPersonalNameByAccountId;
import com.aram.erpcrud.modules.personaldetails.payload.CreatePersonalDetailsCommand;
import com.aram.erpcrud.modules.personaldetails.payload.PersonalDetailsDTO;
import com.aram.erpcrud.modules.personaldetails.payload.PersonalNameDTO;
import com.aram.erpcrud.modules.personaldetails.payload.UpdatePersonalDetailsCommand;
import org.springframework.stereotype.Component;

@Component
public class PersonalDetailsFacade {

    private final GetPersonalNameByAccountId getPersonalNameByAccountIdQueryHandler;
    private final CreatePersonalDetails createPersonalDetailsCommandHandler;
    private final DeleteByAccountId deleteByAccountIdCommandHandler;
    private final UpdatePersonalDetails updatePersonalDetailsCommandHandler;

    public PersonalDetailsFacade(
            GetPersonalNameByAccountId getPersonalNameByAccountIdQueryHandler,
            CreatePersonalDetails createPersonalDetailsCommandHandler,
            DeleteByAccountId deleteByAccountIdCommandHandler,
            UpdatePersonalDetails updatePersonalDetailsCommandHandler
    ) {
        this.getPersonalNameByAccountIdQueryHandler = getPersonalNameByAccountIdQueryHandler;
        this.createPersonalDetailsCommandHandler = createPersonalDetailsCommandHandler;
        this.deleteByAccountIdCommandHandler = deleteByAccountIdCommandHandler;
        this.updatePersonalDetailsCommandHandler = updatePersonalDetailsCommandHandler;
    }

    public PersonalNameDTO getPersonalNameByAccountId(Long id) {
        return getPersonalNameByAccountIdQueryHandler.handle(id);
    }

    public void createPersonalDetails(CreatePersonalDetailsCommand command) {
        createPersonalDetailsCommandHandler.handle(command);
    }

    public void deleteByAccountId(Long id) {
        deleteByAccountIdCommandHandler.handle(id);
    }

    public PersonalDetailsDTO updatePersonalDetails(UpdatePersonalDetailsCommand command) {
        return updatePersonalDetailsCommandHandler.handle(command);
    }
}
