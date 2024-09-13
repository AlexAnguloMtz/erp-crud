package com.aram.erpcrud.users.application;

import com.aram.erpcrud.common.PageResponse;
import com.aram.erpcrud.users.application.command.CreateUserCommandHandler;
import com.aram.erpcrud.users.application.command.UpdateUserCommandHandler;
import com.aram.erpcrud.users.application.command.DeleteUserByIdCommandHandler;
import com.aram.erpcrud.users.application.query.GetMeQueryHandler;
import com.aram.erpcrud.users.application.query.GetUsersQueryHandler;
import com.aram.erpcrud.users.payload.*;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {

    private final CreateUserCommandHandler createUserCommandHandler;
    private final GetUsersQueryHandler getUsersQueryHandler;
    private final GetMeQueryHandler getMeQueryHandler;
    private final UpdateUserCommandHandler updateUserCommandHandler;
    private final DeleteUserByIdCommandHandler deleteUserByIdHandler;

    public UserFacade(
            CreateUserCommandHandler createUserCommandHandler,
            GetUsersQueryHandler getUsersQueryHandler,
            GetMeQueryHandler getMeQueryHandler,
            UpdateUserCommandHandler updateUserCommandHandler,
            DeleteUserByIdCommandHandler deleteUserByIdHandler
    ) {
        this.createUserCommandHandler = createUserCommandHandler;
        this.getUsersQueryHandler = getUsersQueryHandler;
        this.getMeQueryHandler = getMeQueryHandler;
        this.updateUserCommandHandler = updateUserCommandHandler;
        this.deleteUserByIdHandler = deleteUserByIdHandler;
    }

    public void createUser(CreateUserCommand command) {
        createUserCommandHandler.handle(command);
    }

    public UpdateUserResponse updateUser(String id, String requestingUserEmail, UpdateUserCommand command) {
        return updateUserCommandHandler.handle(id, requestingUserEmail, command);
    }

    public PageResponse<FullUserDetails> getUsers(GetUsersQuery getUsersQuery) {
        return getUsersQueryHandler.handle(getUsersQuery);
    }

    public GetMeResponse getMe(String email) {
        return getMeQueryHandler.handle(email);
    }

    public void deleteUserById(String id) {
        deleteUserByIdHandler.handle(id);
    }
}