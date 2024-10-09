package com.aram.erpcrud.modules.auth.payload;

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