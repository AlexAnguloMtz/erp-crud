package com.aram.erpcrud.data;

import org.springframework.stereotype.Component;

@Component
class ApplicationDataSeeder {

    private final StatesSeeder stateSeeder;
    private final RolesSeeder rolesSeeder;
    private final RootUserSeeder rootUserSeeder;
    private final MovementTypeSeeder movementTypeSeeder;

    ApplicationDataSeeder(
            StatesSeeder stateSeeder,
            RolesSeeder rolesSeeder,
            RootUserSeeder rootUserSeeder,
            MovementTypeSeeder movementTypeSeeder
    ) {
        this.stateSeeder = stateSeeder;
        this.rolesSeeder = rolesSeeder;
        this.rootUserSeeder = rootUserSeeder;
        this.movementTypeSeeder = movementTypeSeeder;
    }

    public void seed() throws Exception {
        try {
            doRun();
        } catch (Exception exception) {
            throw new RuntimeException("could not seed application data", exception);
        }
    }

    private void doRun() {
        stateSeeder.seed();
        rolesSeeder.seed();
        rootUserSeeder.seed();
        movementTypeSeeder.seed();
    }
}