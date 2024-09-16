package com.aram.erpcrud.users.application.command;

import com.aram.erpcrud.auth.AuthService;
import com.aram.erpcrud.auth.payload.AccountCreationResponse;
import com.aram.erpcrud.auth.payload.CreateAccountCommand;
import com.aram.erpcrud.locations.LocationsService;
import com.aram.erpcrud.locations.payload.StateDTO;
import com.aram.erpcrud.users.domain.UserAddress;
import com.aram.erpcrud.users.domain.PersonalDetails;
import com.aram.erpcrud.users.domain.PersonalDetailsRepository;
import com.aram.erpcrud.users.payload.CreateUserCommand;
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
        StateDTO stateDto = locationsService.findStateById(command.state());
        PersonalDetails personalDetails = toPersonalDetails(accountCreationResponse.accountId(), command, stateDto.id());
        personalDetailsRepository.save(personalDetails);
    }

    private PersonalDetails toPersonalDetails(String accountId, CreateUserCommand command, String stateId) {
        return PersonalDetails.builder()
                .id(UUID.randomUUID().toString())
                .accountId(accountId)
                .name(command.name())
                .lastName(command.lastName())
                .address(makeAddress(command, stateId))
                .phone(command.phone())
                .build();
    }

    private UserAddress makeAddress(CreateUserCommand command, String stateId) {
        return UserAddress.builder()
                .id(UUID.randomUUID().toString())
                .stateId(stateId)
                .city(command.city())
                .district(command.district())
                .street(command.street())
                .streetNumber(command.streetNumber())
                .zipCode(command.zipCode())
                .build();
    }
}