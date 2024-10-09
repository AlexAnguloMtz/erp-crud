package com.aram.erpcrud.modules.backups.payload;

import lombok.Builder;

import java.time.Instant;

@Builder
public record BackupCreationResponse(
        String backup,
        Instant createdAt
) {
}