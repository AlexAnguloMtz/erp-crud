package com.aram.erpcrud.users.payload;

public record CreateUserCommand(
        String name,
        String lastName,
        String state,
        String city,
        String district,
        String street,
        String streetNumber,
        String zipCode,
        String email,
        String phone,
        String roleId,
        String password
) {}