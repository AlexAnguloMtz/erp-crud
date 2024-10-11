package com.aram.erpcrud.modules.users.payload;

public record UpdateUserResponse(FullUserDTO user, String jwt) {
}
