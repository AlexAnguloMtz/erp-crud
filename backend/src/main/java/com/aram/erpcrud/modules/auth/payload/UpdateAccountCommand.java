package com.aram.erpcrud.modules.auth.payload;

public record UpdateAccountCommand(
        Long id,
        Long roleId,
        String email,
        String requestingUserEmail
) {
}