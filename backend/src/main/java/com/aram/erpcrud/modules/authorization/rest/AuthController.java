package com.aram.erpcrud.modules.authorization.rest;

import com.aram.erpcrud.modules.authorization.application.query.Login;
import com.aram.erpcrud.modules.authorization.domain.Role;
import com.aram.erpcrud.modules.authorization.domain.RoleRepository;
import com.aram.erpcrud.modules.authorization.payload.LoginCommand;
import com.aram.erpcrud.modules.authorization.payload.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final Login loginCommandHandler;
    private final RoleRepository roleRepository;

    public AuthController(
            Login loginCommandHandler,
            RoleRepository roleRepository
    ) {
        this.loginCommandHandler = loginCommandHandler;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid  @RequestBody LoginCommand command) {
        return new ResponseEntity<>(loginCommandHandler.handle(command), HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<Iterable<Role>> getRoles() {
        return new ResponseEntity<>(roleRepository.findAll(), HttpStatus.OK);
    }
}