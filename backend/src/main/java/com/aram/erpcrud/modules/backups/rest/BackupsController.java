package com.aram.erpcrud.modules.backups.rest;

import com.aram.erpcrud.modules.backups.application.BackupFacade;
import com.aram.erpcrud.modules.backups.payload.BackupCreationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/backups")
public class BackupsController {

    private final BackupFacade backupFacade;

    public BackupsController(BackupFacade backupFacade) {
        this.backupFacade = backupFacade;
    }

    @GetMapping
    public ResponseEntity<BackupCreationResponse> newBackup() {
        return new ResponseEntity<>(backupFacade.newBackup(), HttpStatus.OK);
    }

}
