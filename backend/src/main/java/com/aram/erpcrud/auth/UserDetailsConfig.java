package com.aram.erpcrud.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
public class UserDetailsConfig {

    @Bean
    public UserDetailsManager userDetailsManager() {
        return new InMemoryUserDetailsManager(
                User.withUsername("john")
                        .password("{noop}pass")
                        .roles("USER")
                        .build(),
                User.withUsername("daniel")
                        .password("{noop}adminpass")
                        .roles("ADMIN")
                        .build()
        );
    }

}
