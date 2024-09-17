package com.aram.erpcrud.useraggregation.payload;

import com.aram.erpcrud.auth.payload.RolePublicDetails;
import com.aram.erpcrud.users.payload.AddressDTO;

public record FullUserDetails(
        String id,
        String name,
        String lastName,
        AddressDTO address,
        String phone,
        String email,
        RolePublicDetails role
) { }