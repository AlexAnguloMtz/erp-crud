package com.aram.erpcrud.auth.payload;

public record AccountPublicDetails(String id, String email, RolePublicDetails rolePublicDetails) {
}