package com.aram.erpcrud.personaldetails.rest;

import com.aram.erpcrud.common.PageResponse;
import com.aram.erpcrud.personaldetails.payload.*;
import com.aram.erpcrud.personaldetails.application.UserFacade;
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
    public ResponseEntity<UpdateUserResponse> updateUser(
            @Valid @RequestBody UpdateUserCommand command,
            @PathVariable String id,
            Principal principal
    ) {
        return new ResponseEntity<>(usersFacade.updateUser(id, principal.getName(), command), HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<GetMeResponse> getMe(Principal principal) {
        GetMeResponse response = usersFacade.getMe(principal.getName());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") String id) {
        usersFacade.deleteUserById(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
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