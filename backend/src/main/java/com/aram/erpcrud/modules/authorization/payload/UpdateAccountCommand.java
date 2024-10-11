package com.aram.erpcrud.modules.authorization.payload;

public record UpdateAccountCommand(
        Long id,
        Long roleId,
        String email,
        String requestingUserEmail
) {
}