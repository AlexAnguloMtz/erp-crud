package com.aram.erpcrud.modules.backups.payload;

import lombok.Builder;

import java.time.Instant;

@Builder
public record DatabaseBackupCreationResponse(
        String backup,
        Instant createdAt
) {
}