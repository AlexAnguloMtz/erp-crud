package com.aram.erpcrud.personaldetails.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserCommand(
        @NotBlank(message = "Name is required")
        @Size(max = 60, message = "Name cannot exceed 60 characters")
        String name,

        @NotBlank(message = "Last Name is required")
        @Size(max = 60, message = "Last Name cannot exceed 60 characters")
        String lastName,

        @NotBlank(message = "State is required")
        @Size(max = 60, message = "State cannot exceed 60 characters")
        String state,

        @NotBlank(message = "City is required")
        @Size(max = 60, message = "City cannot exceed 60 characters")
        String city,

        @NotBlank(message = "District is required")
        @Size(max = 60, message = "District cannot exceed 60 characters")
        String district,

        @NotBlank(message = "Street is required")
        @Size(max = 60, message = "Street cannot exceed 60 characters")
        String street,

        @NotBlank(message = "Street Number is required")
        @Size(max = 60, message = "Street Number cannot exceed 60 characters")
        String streetNumber,

        @NotBlank(message = "Zip Code is required")
        @Pattern(regexp = "\\d{5}", message = "Zip Code must be exactly 5 digits")
        String zipCode,

        @NotBlank(message = "Email is required")
        @Size(max = 60, message = "Email cannot exceed 60 characters")
        @Email(message = "Email should be valid")
        String email,

        @NotBlank(message = "Phone is required")
        @Pattern(regexp = "\\d{10}", message = "Phone must be exactly 10 digits")
        String phone,

        @NotBlank(message = "Role ID is required")
        @Size(max = 60, message = "Role ID cannot exceed 60 characters")
        String roleId,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 60, message = "Password must be between 8 and 60 characters")
        String password
) {}
