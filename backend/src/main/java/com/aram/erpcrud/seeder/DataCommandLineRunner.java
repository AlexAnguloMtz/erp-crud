package com.aram.erpcrud.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
class DataCommandLineRunner implements CommandLineRunner {

    private final ApplicationDataSeeder applicationDataSeeder;

    public DataCommandLineRunner(
            ApplicationDataSeeder applicationDataSeeder
    ) {
        this.applicationDataSeeder = applicationDataSeeder;
    }

    @Override
    public void run(String... args) throws Exception {
        applicationDataSeeder.seed();
    }
}
