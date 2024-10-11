package com.aram.erpcrud.modules.products.payload;

import lombok.Builder;

@Builder
public record ProductDTO(
        Long id,
        String name,
        BrandDTO brand,
        ProductCategoryDTO productCategory
) {
}
