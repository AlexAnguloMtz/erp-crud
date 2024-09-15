package com.aram.erpcrud.personaldetails.application.command;

import com.aram.erpcrud.auth.AuthService;
import com.aram.erpcrud.auth.payload.AccountCreationResponse;
import com.aram.erpcrud.auth.payload.CreateAccountCommand;
import com.aram.erpcrud.locations.LocationsService;
import com.aram.erpcrud.locations.domain.State;
import com.aram.erpcrud.personaldetails.domain.PersonalDetails;
import com.aram.erpcrud.personaldetails.domain.PersonalDetailsRepository;
import com.aram.erpcrud.personaldetails.payload.CreateUserCommand;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class CreateUserCommandHandler {

    private final AuthService authService;
    private final LocationsService locationsService;
    private final PersonalDetailsRepository personalDetailsRepository;

    public CreateUserCommandHandler(AuthService authService, LocationsService locationsService, PersonalDetailsRepository personalDetailsRepository) {
        this.authService = authService;
        this.locationsService = locationsService;
        this.personalDetailsRepository = personalDetailsRepository;
    }

    @Transactional
    public void handle(CreateUserCommand command) {
        CreateAccountCommand createAccountCommand = new CreateAccountCommand(command.roleId(), command.email(), command.password());
        AccountCreationResponse accountCreationResponse = authService.createAccount(createAccountCommand);
        State state = locationsService.findStateById(command.state());
        PersonalDetails personalDetails = toPersonalDetails(accountCreationResponse.accountId(), command, state);
        personalDetailsRepository.save(personalDetails);
    }

    private PersonalDetails toPersonalDetails(String accountId, CreateUserCommand command, State state) {
        return PersonalDetails.builder()
                .id(UUID.randomUUID().toString())
                .accountId(accountId)
                .name(command.name())
                .lastName(command.lastName())
                .state(state)
                .city(command.city())
                .district(command.district())
                .street(command.street())
                .streetNumber(command.streetNumber())
                .phone(command.phone())
                .zipCode(command.zipCode())
                .build();
    }
}