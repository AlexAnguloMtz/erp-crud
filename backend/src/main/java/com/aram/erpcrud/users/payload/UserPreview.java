package com.aram.erpcrud.users.payload;

public record UserPreview (
        String id,
        String name,
        String lastName,
        String state,
        String city,
        String phone,
        String email,
        String role
) { }
