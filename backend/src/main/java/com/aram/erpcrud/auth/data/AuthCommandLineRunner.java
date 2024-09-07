package com.aram.erpcrud.auth.data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AuthCommandLineRunner implements CommandLineRunner {

    private final RolesSeeder rolesSeeder;
    private final RootUserSeeder rootUserSeeder;

    public AuthCommandLineRunner(RolesSeeder rolesSeeder, RootUserSeeder rootUserSeeder) {
        this.rolesSeeder = rolesSeeder;
        this.rootUserSeeder = rootUserSeeder;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            doRun();
        } catch (Exception exception) {
            throw new RuntimeException("could not seed roles and root user", exception);
        }
    }

    private void doRun() {
        rolesSeeder.seed();
        rootUserSeeder.seed();
    }
}