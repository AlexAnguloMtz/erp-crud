package com.aram.erpcrud.modules.personaldetails.application.command;

import com.aram.erpcrud.modules.personaldetails.application.mapper.PersonalDetailsModelMapper;
import com.aram.erpcrud.modules.personaldetails.domain.PersonalDetails;
import com.aram.erpcrud.modules.personaldetails.domain.PersonalDetailsRepository;
import com.aram.erpcrud.modules.personaldetails.payload.CreatePersonalDetailsCommand;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class CreatePersonalDetailsCommandHandler {

    private final PersonalDetailsModelMapper personalDetailsModelMapper;
    private final PersonalDetailsRepository personalDetailsRepository;

    public CreatePersonalDetailsCommandHandler(
            @Qualifier("personal-details.personal-details-model-mapper") PersonalDetailsModelMapper personalDetailsModelMapper,
            PersonalDetailsRepository personalDetailsRepository
    ) {
        this.personalDetailsModelMapper = personalDetailsModelMapper;
        this.personalDetailsRepository = personalDetailsRepository;
    }

    public void handle(CreatePersonalDetailsCommand command) {
        PersonalDetails personalDetails = personalDetailsModelMapper.toPersonalDetails(command);
        personalDetailsRepository.save(personalDetails);
    }

}