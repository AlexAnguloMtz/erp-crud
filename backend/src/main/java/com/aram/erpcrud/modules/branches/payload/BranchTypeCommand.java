package com.aram.erpcrud.modules.branches.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BranchTypeCommand(

        @NotBlank(message = "Name is required")
        @Size(max = 60, message = "name cannot exceed 60 characters")
        String name,

        @NotBlank(message = "Description is required")
        @Size(max = 100, message = "description cannot exceed 100 characters")
        String description

) {
}