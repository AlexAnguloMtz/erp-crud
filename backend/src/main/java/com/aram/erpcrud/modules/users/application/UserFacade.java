package com.aram.erpcrud.modules.users.application;

import com.aram.erpcrud.modules.users.application.query.GetUsers;
import com.aram.erpcrud.modules.users.payload.GetUsersQuery;
import com.aram.erpcrud.modules.users.application.command.CreateUser;
import com.aram.erpcrud.modules.users.application.command.UpdateUser;
import com.aram.erpcrud.modules.users.application.command.DeleteUserById;
import com.aram.erpcrud.modules.users.application.query.GetMe;
import com.aram.erpcrud.modules.users.payload.*;
import com.aram.erpcrud.utils.PageResponse;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {

    private final CreateUser createUser;
    private final GetMe getMe;
    private final UpdateUser updateUser;
    private final DeleteUserById deleteUserById;
    private final GetUsers getUsers;

    public UserFacade(
            CreateUser createUser,
            GetMe getMe,
            UpdateUser updateUser,
            DeleteUserById deleteUserById,
            GetUsers getUsers
    ) {
        this.createUser = createUser;
        this.getMe = getMe;
        this.updateUser = updateUser;
        this.deleteUserById = deleteUserById;
        this.getUsers = getUsers;
    }

    public void createUser(CreateUserCommand command) {
        createUser.handle(command);
    }

    public UpdateUserResponse updateUser(Long id, String requestingUserEmail, UpdateUserCommand command) {
        return updateUser.handle(id, requestingUserEmail, command);
    }

    public GetMeResponse getMe(String email) {
        return getMe.handle(email);
    }

    public void deleteUserById(Long id) {
        deleteUserById.handle(id);
    }

    public PageResponse<FullUserDTO> handle(GetUsersQuery query) {
        return getUsers.handle(query);
    }
}