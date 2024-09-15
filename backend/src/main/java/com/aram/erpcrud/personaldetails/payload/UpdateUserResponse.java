package com.aram.erpcrud.personaldetails.payload;

public record UpdateUserResponse(FullUserDetails user, String jwt) {
}
