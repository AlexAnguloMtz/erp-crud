package com.aram.erpcrud.seeder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class ApplicationDataSeeder {

    private final RootUserSeeder rootUserSeeder;

    ApplicationDataSeeder(
            RootUserSeeder rootUserSeeder
    ) {
        this.rootUserSeeder = rootUserSeeder;
    }

    public void seed() throws Exception {
        try {
            doRun();
        } catch (Exception exception) {
            throw new RuntimeException("could not seed application data", exception);
        }
    }

    private void doRun() {
        log.info("seeding root user...");
        rootUserSeeder.seed();
        log.info("root user ready");
    }
}