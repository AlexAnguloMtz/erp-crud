package com.aram.erpcrud.users.application;

import com.aram.erpcrud.auth.AuthService;
import com.aram.erpcrud.auth.payload.AccountCreationResponse;
import com.aram.erpcrud.auth.payload.CreateAccountCommand;
import com.aram.erpcrud.users.domain.PersonalDetails;
import com.aram.erpcrud.users.domain.PersonalDetailsRepository;
import com.aram.erpcrud.users.payload.CreateUserCommand;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CreateUserCommandHandler {

    private final AuthService authService;
    private final PersonalDetailsRepository personalDetailsRepository;

    public CreateUserCommandHandler(AuthService authService, PersonalDetailsRepository personalDetailsRepository) {
        this.authService = authService;
        this.personalDetailsRepository = personalDetailsRepository;
    }

    @Transactional
    public void handle(CreateUserCommand command) {
        CreateAccountCommand createAccountCommand = new CreateAccountCommand(command.roleId(), command.email(), command.password());
        AccountCreationResponse accountCreationResponse = authService.createAccount(createAccountCommand);
        PersonalDetails personalDetails = toPersonalDetails(accountCreationResponse.accountId(), command);
        personalDetailsRepository.save(personalDetails);
    }

    private PersonalDetails toPersonalDetails(String accountId, CreateUserCommand command) {
        return new PersonalDetails(
            UUID.randomUUID().toString(),
            accountId,
            command.name(),
            command.lastName(),
            command.state(),
            command.city(),
            command.district(),
            command.streetNumber(),
            command.phone(),
            command.zipCode()
        );
    }
}