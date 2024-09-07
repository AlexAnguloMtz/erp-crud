package com.aram.erpcrud.users.rest;

import com.aram.erpcrud.users.application.UserFacade;
import com.aram.erpcrud.users.payload.CreateUserCommand;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}