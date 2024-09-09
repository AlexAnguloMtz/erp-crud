package com.aram.erpcrud.users.application;

import com.aram.erpcrud.common.PageResponse;
import com.aram.erpcrud.users.payload.*;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {

    private final CreateUserCommandHandler createUserCommandHandler;
    private final GetUsersQueryHandler getUsersQueryHandler;
    private final GetMeQueryHandler getMeQueryHandler;
    private final UpdateUserCommandHandler updateUserCommandHandler;

    public UserFacade(
            CreateUserCommandHandler createUserCommandHandler,
            GetUsersQueryHandler getUsersQueryHandler,
            GetMeQueryHandler getMeQueryHandler, UpdateUserCommandHandler updateUserCommandHandler
    ) {
        this.createUserCommandHandler = createUserCommandHandler;
        this.getUsersQueryHandler = getUsersQueryHandler;
        this.getMeQueryHandler = getMeQueryHandler;
        this.updateUserCommandHandler = updateUserCommandHandler;
    }

    public void createUser(CreateUserCommand command) {
        createUserCommandHandler.handle(command);
    }

    public FullUserDetails updateUser(String id, UpdateUserCommand command) {
        return updateUserCommandHandler.handle(id, command);
    }

    public PageResponse<FullUserDetails> getUsers(GetUsersQuery getUsersQuery) {
        return getUsersQueryHandler.handle(getUsersQuery);
    }

    public GetMeResponse getMe(String email) {
        return getMeQueryHandler.handle(email);
    }
}