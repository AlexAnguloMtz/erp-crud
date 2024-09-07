package com.aram.erpcrud.users.payload;

import com.aram.erpcrud.auth.payload.RolePublicDetails;

public record UserPreview (
        String id,
        String name,
        String lastName,
        String state,
        String city,
        String phone,
        String email,
        RolePublicDetails rolePublicDetails
) { }
