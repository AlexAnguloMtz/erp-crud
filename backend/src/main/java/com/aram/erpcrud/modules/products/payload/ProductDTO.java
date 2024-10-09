package com.aram.erpcrud.modules.products.payload;

public record ProductDTO(
        Long id,
        String name,
        BrandDTO brandDTO,
        ProductCategoryDTO productCategoryDTO
) {
}
