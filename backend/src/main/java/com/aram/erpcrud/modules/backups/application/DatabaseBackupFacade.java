package com.aram.erpcrud.modules.backups.application;

import com.aram.erpcrud.modules.backups.payload.DatabaseBackupCreationResponse;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class DatabaseBackupFacade {

    private final DatabaseBackupGenerator databaseBackupGenerator;

    public DatabaseBackupFacade(DatabaseBackupGenerator backupGenerator) {
        this.databaseBackupGenerator = backupGenerator;
    }

    public DatabaseBackupCreationResponse newBackup() {
        String backupContent = databaseBackupGenerator.newBackup();
        Instant created = Instant.now();
        return new DatabaseBackupCreationResponse(backupContent, created);
    }

}