package com.aram.erpcrud.auth.data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AuthCommandLineRunner implements CommandLineRunner {

    private final StatesSeeder stateSeeder;
    private final RolesSeeder rolesSeeder;
    private final RootUserSeeder rootUserSeeder;

    public AuthCommandLineRunner(
            StatesSeeder stateSeeder,
            RolesSeeder rolesSeeder,
            RootUserSeeder rootUserSeeder
    ) {
        this.stateSeeder = stateSeeder;
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
        stateSeeder.seed();
        rolesSeeder.seed();
        rootUserSeeder.seed();
    }
}