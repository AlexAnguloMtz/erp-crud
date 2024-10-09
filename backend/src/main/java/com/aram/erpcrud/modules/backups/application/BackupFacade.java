package com.aram.erpcrud.modules.backups.application;

import com.aram.erpcrud.modules.backups.payload.BackupCreationResponse;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class BackupFacade {

    private final BackupGenerator backupGenerator;

    public BackupFacade(BackupGenerator backupGenerator) {
        this.backupGenerator = backupGenerator;
    }

    public BackupCreationResponse newBackup() {
        String backupContent = backupGenerator.newBackup();
        Instant created = Instant.now();
        return new BackupCreationResponse(backupContent, created);
    }

}