package com.aram.erpcrud.users.application;

import com.aram.erpcrud.users.payload.CreateUserCommand;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {

    private final CreateUserCommandHandler createUserCommandHandler;

    public UserFacade(CreateUserCommandHandler createUserCommandHandler) {
        this.createUserCommandHandler = createUserCommandHandler;
    }

    public void createUser(CreateUserCommand command) {
        createUserCommandHandler.handle(command);
    }

}
