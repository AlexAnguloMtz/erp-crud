package com.aram.erpcrud.modules.products.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductCategoryCommand(
        @NotBlank(message = "name is required")
        @Size(max = 60, message = "name cannot exceed 60 characters")
        String name
) {
}
