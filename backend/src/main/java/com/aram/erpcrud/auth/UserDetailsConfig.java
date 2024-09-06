package com.aram.erpcrud.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class UserDetailsConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                User.withUsername("john")
                        .password("pass")
                        .roles("USER")
                        .build(),
                User.withUsername("daniel")
                        .password("adminpass")
                        .roles("ADMIN")
                        .build()
        );
    }

}
