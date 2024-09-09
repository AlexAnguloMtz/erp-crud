package com.aram.erpcrud.auth.payload;

import lombok.NonNull;

public record UpdateAccountResponse (
        @NonNull
        String id,

        @NonNull
        String email,

        @NonNull
        RolePublicDetails rolePublicDetails,

        String jwt
) {
}