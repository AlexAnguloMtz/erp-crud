package com.aram.erpcrud.modules.products.application.mapper;

import com.aram.erpcrud.modules.products.domain.Brand;
import com.aram.erpcrud.modules.products.domain.InventoryUnit;
import com.aram.erpcrud.modules.products.domain.Product;
import com.aram.erpcrud.modules.products.domain.ProductCategory;
import com.aram.erpcrud.modules.products.payload.*;
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
                .image(product.getImage())
                .build();
    }

    public ProductCategoryDTO toProductCategoryDTO(ProductCategory productCategory) {
        return new ProductCategoryDTO(productCategory.getId(), productCategory.getName());
    }

    public BrandDTO toBrandDTO(Brand brand) {
        return new BrandDTO(brand.getId(), brand.getName());
    }

    public InventoryUnitDTO toInventoryUnitDTO(InventoryUnit inventoryUnit) {
        return new InventoryUnitDTO(inventoryUnit.getId(), inventoryUnit.getName());
    }

    public Product toProduct(
            CreateProductCommand command,
            Brand brand,
            ProductCategory productCategory,
            InventoryUnit inventoryUnit
    ) {
        return Product.builder()
                .name(command.name())
                .sku(command.sku())
                .salePrice(command.salePrice())
                .brand(brand)
                .productCategory(productCategory)
                .inventoryUnit(inventoryUnit)
                .build();
    }
}