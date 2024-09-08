package com.aram.erpcrud.users.payload;

import com.aram.erpcrud.auth.payload.RolePublicDetails;
import com.aram.erpcrud.locations.payload.StateDTO;

public record FullUserDetails(
        String id,
        String name,
        String lastName,
        StateDTO state,
        String city,
        String district,
        String street,
        String streetNumber,
        String zipCode,
        String phone,
        String email,
        RolePublicDetails role
) { }
