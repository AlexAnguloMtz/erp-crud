package com.aram.erpcrud.users.application;

import com.aram.erpcrud.auth.AuthService;
import com.aram.erpcrud.auth.payload.AccountPublicDetails;
import com.aram.erpcrud.auth.payload.UpdateAccountCommand;
import com.aram.erpcrud.locations.LocationsService;
import com.aram.erpcrud.locations.domain.State;
import com.aram.erpcrud.users.domain.PersonalDetails;
import com.aram.erpcrud.users.domain.PersonalDetailsRepository;
import com.aram.erpcrud.users.payload.UpdateUserCommand;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class UpdateUserCommandHandler {

    private final AuthService authService;
    private final PersonalDetailsRepository personalDetailsRepository;
    private final LocationsService locationsService;

    public UpdateUserCommandHandler(
            AuthService authService,
            PersonalDetailsRepository personalDetailsRepository, LocationsService locationsService
    ) {
        this.authService = authService;
        this.personalDetailsRepository = personalDetailsRepository;
        this.locationsService = locationsService;
    }

    @Transactional
    public void handle(String id, UpdateUserCommand command) {
        Optional<AccountPublicDetails> accountOptional = authService.findAccountById(id);
        if (accountOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Optional<PersonalDetails> personalDetailsOptional = personalDetailsRepository.findByAccountId(id);
        if (personalDetailsOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        State state = locationsService.findStateById(command.state());

        UpdateAccountCommand updateAccountCommand = toAccountPublicDetails(id, command);
        authService.updateAccount(updateAccountCommand);

        PersonalDetails personalDetails = personalDetailsOptional.get();

        personalDetails.setName(command.name());
        personalDetails.setLastName(command.lastName());
        personalDetails.setState(state);
        personalDetails.setCity(command.city());
        personalDetails.setDistrict(command.district());
        personalDetails.setStreet(command.street());
        personalDetails.setStreetNumber(command.streetNumber());
        personalDetails.setZipCode(command.zipCode());
        personalDetails.setPhone(command.phone());

        personalDetailsRepository.save(personalDetails);
    }

    private UpdateAccountCommand toAccountPublicDetails(String id, UpdateUserCommand command) {
        return new UpdateAccountCommand(
            id,
            command.email(),
            command.roleId()
        );
    }
}