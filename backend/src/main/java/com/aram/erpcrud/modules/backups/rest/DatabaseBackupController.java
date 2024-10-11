package com.aram.erpcrud.modules.backups.rest;

import com.aram.erpcrud.modules.backups.application.DatabaseBackupFacade;
import com.aram.erpcrud.modules.backups.payload.DatabaseBackupCreationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/backups")
public class DatabaseBackupController {

    private final DatabaseBackupFacade databaseBackupFacade;

    public DatabaseBackupController(DatabaseBackupFacade backupFacade) {
        this.databaseBackupFacade = backupFacade;
    }

    @GetMapping
    public ResponseEntity<DatabaseBackupCreationResponse> newBackup() {
        return new ResponseEntity<>(databaseBackupFacade.newBackup(), HttpStatus.OK);
    }

}