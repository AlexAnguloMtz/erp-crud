package com.aram.erpcrud.modules.products.payload;

import jakarta.validation.constraints.*;

public record UpdateProductCommand(

        @NotBlank(message = "Name is required")
        @Size(max = 60, message = "name cannot exceed 60 characters")
        String name,

        @NotBlank(message = "SKU is required")
        @Size(min = 8, max = 8, message = "SKU must be exactly 8 characters long")
        String sku,

        @NotNull(message = "Sale Price is required")
        @Min(value = 1, message = "Sale Price must be greater than zero")
        @Max(value = 9999999, message = "Sale Price must be less than 10,000,000")
        Integer salePrice,

        @NotNull(message = "Brand id is required")
        Long brandId,

        @NotNull(message = "Product Category id is required")
        Long productCategoryId,

        @NotNull(message = "Inventory Unit id is required")
        Long inventoryUnitId,

        @NotBlank(message = "Image action is required")
        String imageAction

) { }