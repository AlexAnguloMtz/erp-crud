package com.aram.erpcrud.users.payload;

public record UpdateUserResponse(FullUserDetails user, String jwt) {
}
