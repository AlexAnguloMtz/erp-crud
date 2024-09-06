package com.aram.erpcrud.auth.payload;

public record LoginCommand(String email, String password) { }