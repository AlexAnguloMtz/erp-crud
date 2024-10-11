package com.aram.erpcrud.modules.authorization.payload;

public record AccountPublicDetails(
        Long id,
        String email,
        RolePublicDetails rolePublicDetails
) {
}