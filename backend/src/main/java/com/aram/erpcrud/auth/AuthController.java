package com.aram.erpcrud.auth;

import com.aram.erpcrud.auth.payload.LoginCredentials;
import com.aram.erpcrud.auth.payload.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserDetailsService userDetailsService;

    public AuthController(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginCredentials credentials) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(credentials.username());
        if (userDetails == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Bad user");
        }

        if (!userDetails.getPassword().equals(credentials.password())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Bad password");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new LoginResponse("this is your token"));
    }
}