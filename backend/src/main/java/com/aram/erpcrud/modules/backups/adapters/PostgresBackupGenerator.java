package com.aram.erpcrud.modules.backups.adapters;

import com.aram.erpcrud.modules.backups.application.BackupGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component
public class PostgresBackupGenerator implements BackupGenerator {

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.url}")
    private String url;

    @Override
    public String newBackup() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(
                "pg_dump",
                "-U", user,
                "-h", databaseHost(),
                "-p", databasePort(),
                "-d", databaseName()
        );

        // Set the environment variable for password
        processBuilder.environment().put("PGPASSWORD", password);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

        try {
            Process process = processBuilder.start();
            InputStream inputStream = process.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;

            // Read the output from the command
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // Wait for the process to complete
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                String errorMessage = errorStream.toString(); // Get the error output as a string
                log.error("Backup process failed with exit code: {}. Error: {}", exitCode, errorMessage);
            }

        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
        }

        return outputStream.toString();
    }

    private String databaseHost() {
        // Assuming the URL is in the format: jdbc:postgresql://host:port/database
        String[] parts = url.split("://");
        if (parts.length < 2) {
            log.error("Could not parse database host");
            return "";
        }

        String hostPortPart = parts[1].split("/")[0]; // Get the part after "jdbc:postgresql://"
        // Get the host part before ":"
        return hostPortPart.split(":")[0];
    }

    private String databasePort() {
        // Assuming the URL is in the format: jdbc:postgresql://host:port/database
        String[] parts = url.split("://");
        if (parts.length < 2) {
            log.error("Could not parse database port");
            return "";
        }

        String hostPortPart = parts[1].split("/")[0]; // Get the part after "jdbc:postgresql://"
        String[] hostPort = hostPortPart.split(":"); // Split by ":"

        return hostPort.length > 1 ? hostPort[1] : "5432"; // Default to 5432 if port is not specified
    }

    private String databaseName() {
        // Assuming the URL is in the format: jdbc:postgresql://host:port/database
        String[] parts = url.split("/");
        return parts.length > 0 ? parts[parts.length - 1] : "";
    }
}