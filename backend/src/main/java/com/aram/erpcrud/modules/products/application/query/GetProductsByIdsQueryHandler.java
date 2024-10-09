package com.aram.erpcrud.modules.products.application.query;

import com.aram.erpcrud.modules.products.domain.Brand;
import com.aram.erpcrud.modules.products.domain.Product;
import com.aram.erpcrud.modules.products.domain.ProductCategory;
import com.aram.erpcrud.modules.products.domain.ProductRepository;
import com.aram.erpcrud.modules.products.payload.BrandDTO;
import com.aram.erpcrud.modules.products.payload.ProductCategoryDTO;
import com.aram.erpcrud.modules.products.payload.ProductDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetProductsByIdsQueryHandler {

    private final ProductRepository productRepository;

    public GetProductsByIdsQueryHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDTO> handle(List<Long> ids) {
        return productRepository.findAllById(ids).stream()
                .map(this::toProductDto)
                .toList();
    }

    private ProductDTO toProductDto(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                toBrandDto(product.getBrand()),
                toProductCategoryDto(product.getProductCategory())
        );
    }

    private BrandDTO toBrandDto(Brand brand) {
        return new BrandDTO(brand.getId(), brand.getName());
    }

    private ProductCategoryDTO toProductCategoryDto(ProductCategory productCategory) {
        return new ProductCategoryDTO(productCategory.getId(), productCategory.getName());
    }

}
