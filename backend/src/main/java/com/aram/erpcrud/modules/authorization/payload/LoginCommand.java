package com.aram.erpcrud.modules.authorization.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginCommand(
        @NotBlank(message = "Email is required")
        @Size(max = 60, message = "Email cannot exceed 60 characters")
        @Email(message = "Email should be valid")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 60, message = "Password must be between 8 and 60 characters")
        String password
) { }