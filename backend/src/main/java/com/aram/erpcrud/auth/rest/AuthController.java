package com.aram.erpcrud.auth.rest;

import com.aram.erpcrud.auth.application.LoginCommandHandler;
import com.aram.erpcrud.auth.payload.LoginCommand;
import com.aram.erpcrud.auth.payload.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final LoginCommandHandler loginCommandHandler;

    public AuthController(LoginCommandHandler loginCommandHandler) {
        this.loginCommandHandler = loginCommandHandler;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid  @RequestBody LoginCommand command) {
        return new ResponseEntity<>(loginCommandHandler.handle(command), HttpStatus.OK);
    }
}