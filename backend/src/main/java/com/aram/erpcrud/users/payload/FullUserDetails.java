package com.aram.erpcrud.users.payload;

import com.aram.erpcrud.auth.payload.RolePublicDetails;

public record FullUserDetails(
        String id,
        String name,
        String lastName,
        AddressDTO address,
        String phone,
        String email,
        RolePublicDetails role
) { }