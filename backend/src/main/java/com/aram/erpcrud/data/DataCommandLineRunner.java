package com.aram.erpcrud.data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@Component
class DataCommandLineRunner implements CommandLineRunner {

    private final ApplicationDataSeeder applicationDataSeeder;
    private final TestDataSeeder testDataSeeder;
    private final Environment environment;

    public DataCommandLineRunner(
            ApplicationDataSeeder applicationDataSeeder,
            TestDataSeeder testDataSeeder,
            Environment environment
    ) {
        this.applicationDataSeeder = applicationDataSeeder;
        this.testDataSeeder = testDataSeeder;
        this.environment = environment;
    }

    @Override
    public void run(String... args) throws Exception {
        applicationDataSeeder.seed();

        if (Arrays.asList(environment.getActiveProfiles()).contains("test")) {
            testDataSeeder.seed();
        }
    }
}
