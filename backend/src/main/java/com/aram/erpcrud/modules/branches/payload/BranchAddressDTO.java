package com.aram.erpcrud.modules.branches.payload;

import lombok.Builder;

@Builder
public record BranchAddressDTO(
        Long id,
        String district,
        String street,
        String streetNumber,
        String zipCode
) {
}