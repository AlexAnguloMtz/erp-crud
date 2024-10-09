package com.aram.erpcrud.modules.personaldetails.payload;

import lombok.Builder;

@Builder
public record AddressDTO(
        Long id,
        String district,
        String street,
        String streetNumber,
        String zipCode
) {
}