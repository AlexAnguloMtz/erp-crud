package com.aram.erpcrud.modules.personaldetails.application;

import com.aram.erpcrud.modules.personaldetails.application.command.CreatePersonalDetailsCommandHandler;
import com.aram.erpcrud.modules.personaldetails.application.command.DeleteByAccountIdCommandHandler;
import com.aram.erpcrud.modules.personaldetails.application.command.UpdatePersonalDetailsCommandHandler;
import com.aram.erpcrud.modules.personaldetails.application.query.GetPersonalNameByAccountIdQueryHandler;
import com.aram.erpcrud.modules.personaldetails.payload.CreatePersonalDetailsCommand;
import com.aram.erpcrud.modules.personaldetails.payload.PersonalDetailsDTO;
import com.aram.erpcrud.modules.personaldetails.payload.PersonalNameDTO;
import com.aram.erpcrud.modules.personaldetails.payload.UpdatePersonalDetailsCommand;
import org.springframework.stereotype.Component;

@Component
public class PersonalDetailsFacade {

    private final GetPersonalNameByAccountIdQueryHandler getPersonalNameByAccountIdQueryHandler;
    private final CreatePersonalDetailsCommandHandler createPersonalDetailsCommandHandler;
    private final DeleteByAccountIdCommandHandler deleteByAccountIdCommandHandler;
    private final UpdatePersonalDetailsCommandHandler updatePersonalDetailsCommandHandler;

    public PersonalDetailsFacade(
            GetPersonalNameByAccountIdQueryHandler getPersonalNameByAccountIdQueryHandler,
            CreatePersonalDetailsCommandHandler createPersonalDetailsCommandHandler,
            DeleteByAccountIdCommandHandler deleteByAccountIdCommandHandler,
            UpdatePersonalDetailsCommandHandler updatePersonalDetailsCommandHandler
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
