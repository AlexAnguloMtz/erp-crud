package com.aram.erpcrud.modules.users.application.command;

import com.aram.erpcrud.modules.auth.AuthService;
import com.aram.erpcrud.modules.auth.payload.AccountCreationResponse;
import com.aram.erpcrud.modules.auth.payload.CreateAccountCommand;
import com.aram.erpcrud.modules.personaldetails.PersonalDetailsService;
import com.aram.erpcrud.modules.personaldetails.payload.CreatePersonalDetailsCommand;
import com.aram.erpcrud.modules.users.application.mapper.PersonalDetailsModelMapper;
import com.aram.erpcrud.modules.users.payload.CreateUserCommand;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateUserCommandHandler {

    private final AuthService authService;
    private final PersonalDetailsService personalDetailsService;
    private final PersonalDetailsModelMapper personalDetailsModelMapper;

    public CreateUserCommandHandler(
            AuthService authService,
            PersonalDetailsService personalDetailsService,
            @Qualifier("users.personal-details-model-mapper") PersonalDetailsModelMapper personalDetailsModelMapper
    ) {
        this.authService = authService;
        this.personalDetailsService = personalDetailsService;
        this.personalDetailsModelMapper = personalDetailsModelMapper;
    }

    @Transactional
    public void handle(CreateUserCommand command) {
        CreateAccountCommand createAccountCommand =
                new CreateAccountCommand(command.roleId(), command.email(), command.password());

        AccountCreationResponse accountCreationResponse = authService.createAccount(createAccountCommand);

        CreatePersonalDetailsCommand createPersonalDetailsCommand =
                personalDetailsModelMapper.toPersonalDetailsCreationCommand(accountCreationResponse.accountId(), command);

        personalDetailsService.createPersonalDetails(createPersonalDetailsCommand);
    }

}