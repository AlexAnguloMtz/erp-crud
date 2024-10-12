package com.aram.erpcrud.modules.branches.payload;

import lombok.Builder;

@Builder
public record BranchTypeDTO(
        Long id,
        String name,
        String description
) {
}