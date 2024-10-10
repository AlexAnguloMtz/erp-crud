package com.aram.erpcrud.modules.productdetails.payload;

public record ProductDTO(
        Long id,
        String name,
        BrandDTO brandDTO,
        ProductCategoryDTO productCategoryDTO
) {
}
