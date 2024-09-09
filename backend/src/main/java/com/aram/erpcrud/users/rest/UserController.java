package com.aram.erpcrud.users.rest;

import com.aram.erpcrud.common.PageResponse;
import com.aram.erpcrud.users.payload.UpdateUserCommand;
import com.aram.erpcrud.users.application.UserFacade;
import com.aram.erpcrud.users.payload.GetMeResponse;
import com.aram.erpcrud.users.payload.FullUserDetails;
import com.aram.erpcrud.users.payload.CreateUserCommand;
import com.aram.erpcrud.users.payload.GetUsersQuery;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserFacade usersFacade;

    public UserController(UserFacade usersFacade) {
        this.usersFacade = usersFacade;
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody CreateUserCommand command) {
        usersFacade.createUser(command);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FullUserDetails> updateUser(
            @Valid @RequestBody UpdateUserCommand command,
            @PathVariable String id
    ) {
        return new ResponseEntity<>(usersFacade.updateUser(id, command), HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<GetMeResponse> getMe(Principal principal) {
        GetMeResponse response = usersFacade.getMe(principal.getName());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageResponse<FullUserDetails>> getUsers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @Min(0) Integer pageNumber,
            @RequestParam(required = false) @Min(1) @Max(50) Integer pageSize,
            @RequestParam(required = false) String sort
    ) {
        GetUsersQuery getUsersQuery = new GetUsersQuery(search, pageNumber, pageSize, sort);
        return ResponseEntity.ok(usersFacade.getUsers(getUsersQuery));
    }
}