package com.aram.erpcrud.auth.application;

import com.aram.erpcrud.auth.config.JwtHandler;
import com.aram.erpcrud.auth.domain.AuthUser;
import com.aram.erpcrud.auth.domain.AuthUserRepository;
import com.aram.erpcrud.auth.payload.LoginCommand;
import com.aram.erpcrud.auth.payload.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class LoginCommandHandler {

    private final PasswordEncoder passwordEncoder;
    private final AuthUserRepository authUserRepository;
    private final JwtHandler jwtHandler;

    public LoginCommandHandler(
            PasswordEncoder passwordEncoder,
            AuthUserRepository authUserRepository,
            JwtHandler jwtHandler
    ) {
        this.passwordEncoder = passwordEncoder;
        this.authUserRepository = authUserRepository;
        this.jwtHandler = jwtHandler;
    }

    public LoginResponse handle(LoginCommand command) {
        Optional<AuthUser> userOptional = authUserRepository.findByEmail(command.email());
        if (userOptional.isEmpty()) {
            throw new BadCredentialsException("bad credentials");
        }

        AuthUser user = userOptional.get();
        if (!passwordEncoder.matches(command.password(), user.getPassword())) {
            throw new BadCredentialsException("bad credentials");
        }

        String token = jwtHandler.createToken(user.getEmail(), user.getPassword());
        return new LoginResponse(token);
    }

}
