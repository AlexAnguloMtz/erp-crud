package com.aram.erpcrud.modules.productdetails.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BrandCommand(
        @NotBlank(message = "name is required")
        @Size(max = 60, message = "name cannot exceed 60 characters")
        String name
) {
}