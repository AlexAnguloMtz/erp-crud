package com.aram.erpcrud.modules.personaldetails.payload;

import lombok.Builder;

@Builder
public record CreateUserAddressCommand(
        String district,
        String street,
        String streetNumber,
        String zipCode
) {
}
