package com.aram.erpcrud.modules.branches.payload;

import lombok.Builder;

@Builder
public record BranchDTO(
        Long id,
        String name,
        String phone,
        String image,
        BranchAddressDTO address,
        BranchTypeDTO branchType
) {
}
