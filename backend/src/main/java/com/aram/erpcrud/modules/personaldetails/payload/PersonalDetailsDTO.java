package com.aram.erpcrud.modules.personaldetails.payload;

import lombok.Builder;

@Builder
public record PersonalDetailsDTO(
        String name,
        String lastName,
        String phone,
        AddressDTO address
) {
}
