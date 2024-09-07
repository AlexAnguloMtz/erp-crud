package com.aram.erpcrud.auth.payload;

public record CreateAccountCommand(String roleId, String email, String password) { }
