package com.aram.erpcrud.modules.products.application.mapper;

import com.aram.erpcrud.modules.products.domain.Brand;
import com.aram.erpcrud.modules.products.domain.InventoryUnit;
import com.aram.erpcrud.modules.products.domain.Product;
import com.aram.erpcrud.modules.products.domain.ProductCategory;
import com.aram.erpcrud.modules.products.payload.BrandDTO;
import com.aram.erpcrud.modules.products.payload.InventoryUnitDTO;
import com.aram.erpcrud.modules.products.payload.ProductCategoryDTO;
import com.aram.erpcrud.modules.products.payload.ProductDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductModelMapper {

    public ProductDTO toProductDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .sku(product.getSku())
                .salePrice(product.getSalePrice())
                .brand(toBrandDTO(product.getBrand()))
                .productCategory(toProductCategoryDTO(product.getProductCategory()))
                .inventoryUnit(toInventoryUnitDTO(product.getInventoryUnit()))
                .build();
    }

    public ProductCategoryDTO toProductCategoryDTO(ProductCategory productCategory) {
        return new ProductCategoryDTO(productCategory.getId(), productCategory.getName());
    }

    public BrandDTO toBrandDTO(Brand brand) {
        return new BrandDTO(brand.getId(), brand.getName());
    }

    private InventoryUnitDTO toInventoryUnitDTO(InventoryUnit inventoryUnit) {
        return new InventoryUnitDTO(inventoryUnit.getId(), inventoryUnit.getName());
    }

}