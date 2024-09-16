package com.aram.erpcrud.users.payload;

import com.aram.erpcrud.locations.payload.StateDTO;

public record AddressDTO(
        String id,
        StateDTO state,
        String city,
        String district,
        String street,
        String streetNumber,
        String zipCode
) {
}