package com.aram.erpcrud.auth.config;

import com.aram.erpcrud.auth.domain.AuthUser;
import com.aram.erpcrud.auth.domain.AuthUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DatabaseUserDetailsService implements UserDetailsService {

    private final AuthUserRepository authUserRepository;

    public DatabaseUserDetailsService(AuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<AuthUser> userDetailsOptional = authUserRepository.findByEmail(email);
        if (userDetailsOptional.isEmpty()) {
            throw new UsernameNotFoundException("user not found");
        }
        return new AuthUserDetails(userDetailsOptional.get());
    }
}