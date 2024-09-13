package com.aram.erpcrud.auth.rest;

import com.aram.erpcrud.auth.application.command.LoginCommandHandler;
import com.aram.erpcrud.auth.domain.AuthRole;
import com.aram.erpcrud.auth.domain.AuthRoleRepository;
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
    private final AuthRoleRepository authRoleRepository;

    public AuthController(LoginCommandHandler loginCommandHandler, AuthRoleRepository authRoleRepository) {
        this.loginCommandHandler = loginCommandHandler;
        this.authRoleRepository = authRoleRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid  @RequestBody LoginCommand command) {
        return new ResponseEntity<>(loginCommandHandler.handle(command), HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<Iterable<AuthRole>> getRoles() {
        return new ResponseEntity<>(authRoleRepository.findAll(), HttpStatus.OK);
    }
}