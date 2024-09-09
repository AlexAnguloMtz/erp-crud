package com.aram.erpcrud.auth.payload;

public record UpdateAccountCommand(
        String id,
        String email,
        String roleId
) {
}