package com.aram.erpcrud.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
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
        log.error("seeding states...");
        stateSeeder.seed();
        log.error("states ready");

        log.error("seeding roles...");
        rolesSeeder.seed();
        log.error("roles ready");

        log.error("seeding root user...");
        rootUserSeeder.seed();
        log.error("root user ready");

        log.error("seeding stock movement types...");
        movementTypeSeeder.seed();
        log.error("stock movement types ready");
    }
}