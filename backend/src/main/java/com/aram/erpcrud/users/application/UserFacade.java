package com.aram.erpcrud.users.application;

import com.aram.erpcrud.common.PageResponse;
import com.aram.erpcrud.users.payload.GetMeResponse;
import com.aram.erpcrud.users.payload.FullUserDetails;
import com.aram.erpcrud.users.payload.CreateUserCommand;
import com.aram.erpcrud.users.payload.GetUsersQuery;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {

    private final CreateUserCommandHandler createUserCommandHandler;
    private final GetUsersQueryHandler getUsersQueryHandler;
    private final GetMeQueryHandler getMeQueryHandler;

    public UserFacade(
            CreateUserCommandHandler createUserCommandHandler,
            GetUsersQueryHandler getUsersQueryHandler,
            GetMeQueryHandler getMeQueryHandler
    ) {
        this.createUserCommandHandler = createUserCommandHandler;
        this.getUsersQueryHandler = getUsersQueryHandler;
        this.getMeQueryHandler = getMeQueryHandler;
    }

    public void createUser(CreateUserCommand command) {
        createUserCommandHandler.handle(command);
    }

    public PageResponse<FullUserDetails> getUsers(GetUsersQuery getUsersQuery) {
        return getUsersQueryHandler.handle(getUsersQuery);
    }

    public GetMeResponse getMe(String email) {
        return getMeQueryHandler.handle(email);
    }
}