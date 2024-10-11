package com.aram.erpcrud.modules.authorization.payload;

public record UpdateAccountResponse (
        String email,
        RolePublicDetails rolePublicDetails,
        String jwt
) {
}