package com.aram.erpcrud.modules.personaldetails.payload;

import lombok.Builder;

@Builder
public record UpdateAddressCommand(
        String street,
        String streetNumber,
        String district,
        String zipCode
) {
}