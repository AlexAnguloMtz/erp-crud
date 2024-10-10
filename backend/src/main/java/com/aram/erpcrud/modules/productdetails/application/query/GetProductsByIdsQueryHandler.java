package com.aram.erpcrud.modules.productdetails.application.query;

import com.aram.erpcrud.modules.productdetails.domain.Brand;
import com.aram.erpcrud.modules.productdetails.domain.Product;
import com.aram.erpcrud.modules.productdetails.domain.ProductCategory;
import com.aram.erpcrud.modules.productdetails.domain.ProductRepository;
import com.aram.erpcrud.modules.productdetails.payload.BrandDTO;
import com.aram.erpcrud.modules.productdetails.payload.ProductCategoryDTO;
import com.aram.erpcrud.modules.productdetails.payload.ProductDTO;
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
