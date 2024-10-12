package com.aram.erpcrud.modules.branches.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record BranchCommand(
        @NotBlank(message = "Name is required")
        @Size(max = 60, message = "name cannot exceed 60 characters")
        String name,

        @NotBlank(message = "Phone is required")
        @Pattern(regexp = "\\d{10}", message = "Phone must be exactly 10 digits")
        String phone,

        @NotBlank(message = "District is required")
        @Size(max = 60, message = "District cannot exceed 60 characters")
        String district,

        @NotBlank(message = "Street is required")
        @Size(max = 60, message = "Street cannot exceed 60 characters")
        String street,

        @NotBlank(message = "Street Number is required")
        @Size(max = 10, message = "Street Number cannot exceed 10 characters")
        String streetNumber,

        @NotBlank(message = "Zip Code is required")
        @Pattern(regexp = "\\d{5}", message = "Zip Code must be exactly 5 digits")
        String zipCode,

        @NotNull(message = "Branch Type id is required")
        Long branchTypeId
) {
}