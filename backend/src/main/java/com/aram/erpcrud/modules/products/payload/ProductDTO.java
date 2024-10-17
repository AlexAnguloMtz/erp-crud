package com.aram.erpcrud.modules.products.payload;

import lombok.Builder;

@Builder
public record ProductDTO(
        Long id,
        String name,
        String sku,
        Integer salePrice,
        String image,
        BrandDTO brand,
        ProductCategoryDTO productCategory,
        InventoryUnitDTO inventoryUnit
) {
}
