package com.aram.erpcrud.products.payload;

public record ProductDTO(
        String id,
        String name,
        BrandDTO brandDTO,
        ProductCategoryDTO productCategoryDTO
) {
}
