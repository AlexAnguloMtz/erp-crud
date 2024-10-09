package com.aram.erpcrud.modules.auth.payload;

public record AccountPublicDetails(
        Long id,
        String email,
        RolePublicDetails rolePublicDetails
) {
}