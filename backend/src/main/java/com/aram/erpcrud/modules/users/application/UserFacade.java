package com.aram.erpcrud.modules.users.application;

import com.aram.erpcrud.modules.users.application.query.GetUsersQueryHandler;
import com.aram.erpcrud.modules.users.payload.GetUsersQuery;
import com.aram.erpcrud.modules.users.application.command.CreateUserCommandHandler;
import com.aram.erpcrud.modules.users.application.command.UpdateUserCommandHandler;
import com.aram.erpcrud.modules.users.application.command.DeleteUserByIdCommandHandler;
import com.aram.erpcrud.modules.users.application.query.GetMeQueryHandler;
import com.aram.erpcrud.modules.users.payload.*;
import com.aram.erpcrud.utils.PageResponse;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {

    private final CreateUserCommandHandler createUserCommandHandler;
    private final GetMeQueryHandler getMeQueryHandler;
    private final UpdateUserCommandHandler updateUserCommandHandler;
    private final DeleteUserByIdCommandHandler deleteUserByIdHandler;
    private final GetUsersQueryHandler getUsersQueryHandler;

    public UserFacade(
            CreateUserCommandHandler createUserCommandHandler,
            GetMeQueryHandler getMeQueryHandler,
            UpdateUserCommandHandler updateUserCommandHandler,
            DeleteUserByIdCommandHandler deleteUserByIdHandler,
            GetUsersQueryHandler getUsersQueryHandler
    ) {
        this.createUserCommandHandler = createUserCommandHandler;
        this.getMeQueryHandler = getMeQueryHandler;
        this.updateUserCommandHandler = updateUserCommandHandler;
        this.deleteUserByIdHandler = deleteUserByIdHandler;
        this.getUsersQueryHandler = getUsersQueryHandler;
    }

    public void createUser(CreateUserCommand command) {
        createUserCommandHandler.handle(command);
    }

    public UpdateUserResponse updateUser(Long id, String requestingUserEmail, UpdateUserCommand command) {
        return updateUserCommandHandler.handle(id, requestingUserEmail, command);
    }

    public GetMeResponse getMe(String email) {
        return getMeQueryHandler.handle(email);
    }

    public void deleteUserById(Long id) {
        deleteUserByIdHandler.handle(id);
    }

    public PageResponse<FullUserDTO> handle(GetUsersQuery query) {
        return getUsersQueryHandler.handle(query);
    }
}