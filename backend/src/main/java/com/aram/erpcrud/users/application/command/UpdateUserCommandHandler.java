package com.aram.erpcrud.users.application.command;

import com.aram.erpcrud.auth.AuthService;
import com.aram.erpcrud.auth.config.JwtHandler;
import com.aram.erpcrud.auth.payload.AccountPublicDetails;
import com.aram.erpcrud.auth.payload.UpdateAccountCommand;
import com.aram.erpcrud.auth.payload.UpdateAccountResponse;
import com.aram.erpcrud.locations.LocationsService;
import com.aram.erpcrud.locations.domain.State;
import com.aram.erpcrud.locations.payload.StateDTO;
import com.aram.erpcrud.users.domain.Address;
import com.aram.erpcrud.users.domain.PersonalDetails;
import com.aram.erpcrud.users.domain.PersonalDetailsRepository;
import com.aram.erpcrud.users.payload.AddressDTO;
import com.aram.erpcrud.users.payload.FullUserDetails;
import com.aram.erpcrud.users.payload.UpdateUserCommand;
import com.aram.erpcrud.users.payload.UpdateUserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class UpdateUserCommandHandler {

    private final AuthService authService;
    private final PersonalDetailsRepository personalDetailsRepository;
    private final LocationsService locationsService;

    public UpdateUserCommandHandler(
            AuthService authService,
            PersonalDetailsRepository personalDetailsRepository, LocationsService locationsService, JwtHandler jwtHandler
    ) {
        this.authService = authService;
        this.personalDetailsRepository = personalDetailsRepository;
        this.locationsService = locationsService;
    }

    @Transactional
    public UpdateUserResponse handle(String id, String requestingUserEmail, UpdateUserCommand command) {
        Optional<AccountPublicDetails> accountOptional = authService.findAccountById(id);
        if (accountOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Optional<PersonalDetails> personalDetailsOptional = personalDetailsRepository.findByAccountId(id);
        if (personalDetailsOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        State state = locationsService.findStateById(command.state());

        UpdateAccountCommand updateAccountCommand = toAccountPublicDetails(id, requestingUserEmail, command);
        UpdateAccountResponse updateAccountResponse = authService.updateAccount(updateAccountCommand);

        PersonalDetails personalDetails = personalDetailsOptional.get();

        personalDetails.setName(command.name());
        personalDetails.setLastName(command.lastName());
        personalDetails.setPhone(command.phone());

        Address updatedAddress = Address.builder()
                .id(personalDetails.getAddress().getId())
                .state(state)
                .city(command.city())
                .district(command.district())
                .street(command.street())
                .streetNumber(command.streetNumber())
                .zipCode(command.zipCode())
                .build();

        personalDetails.setAddress(updatedAddress);

        PersonalDetails savedPersonalDetails = personalDetailsRepository.save(personalDetails);

        FullUserDetails fullUserDetails = new FullUserDetails(
                id,
                savedPersonalDetails.getName(),
                savedPersonalDetails.getLastName(),
                toAddressDto(savedPersonalDetails.getAddress()),
                savedPersonalDetails.getPhone(),
                updateAccountResponse.email(),
                updateAccountResponse.rolePublicDetails()
        );

        return new UpdateUserResponse(fullUserDetails, updateAccountResponse.jwt());
    }

    private AddressDTO toAddressDto(Address address) {
        return new AddressDTO(
                address.getId(),
                toStateDto(address.getState()),
                address.getCity(),
                address.getDistrict(),
                address.getStreet(),
                address.getStreetNumber(),
                address.getZipCode()
        );
    }

    private UpdateAccountCommand toAccountPublicDetails(String id, String requestingUserEmail, UpdateUserCommand command) {
        return new UpdateAccountCommand(
            id,
            command.email(),
            command.roleId(),
            requestingUserEmail
        );
    }

    private StateDTO toStateDto(State state) {
        return new StateDTO(state.getId(), state.getName());
    }
}