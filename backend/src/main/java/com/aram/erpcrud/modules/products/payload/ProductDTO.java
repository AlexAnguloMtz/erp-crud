package com.aram.erpcrud.modules.products.payload;

import lombok.Builder;

@Builder
public record ProductDTO(
        Long id,
        String name,
        String sku,
        Integer salePrice,
        BrandDTO brand,
        ProductCategoryDTO productCategory,
        InventoryUnitDTO inventoryUnit
) {
}
