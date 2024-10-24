package com.aram.erpcrud.modules.authorization.application.query;

import com.aram.erpcrud.modules.authorization.util.JwtHandler;
import com.aram.erpcrud.modules.authorization.domain.Account;
import com.aram.erpcrud.modules.authorization.domain.AccountRepository;
import com.aram.erpcrud.modules.authorization.payload.LoginCommand;
import com.aram.erpcrud.modules.authorization.payload.LoginResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Login {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final JwtHandler jwtHandler;

    public Login(
            PasswordEncoder passwordEncoder,
            AccountRepository accountRepository,
            JwtHandler jwtHandler
    ) {
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
        this.jwtHandler = jwtHandler;
    }

    public LoginResponse handle(LoginCommand command) {
        Optional<Account> userOptional = accountRepository.findByEmail(command.email());
        if (userOptional.isEmpty()) {
            throw new BadCredentialsException("bad credentials");
        }

        Account user = userOptional.get();
        if (!passwordEncoder.matches(command.password(), user.getPassword())) {
            throw new BadCredentialsException("bad credentials");
        }

        String token = jwtHandler.createToken(user.getEmail(), user.getRole().getCanonicalName());

        return new LoginResponse(token);
    }

}
