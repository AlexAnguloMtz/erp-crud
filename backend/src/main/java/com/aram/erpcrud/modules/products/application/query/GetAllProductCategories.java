package com.aram.erpcrud.modules.products.application.query;

import com.aram.erpcrud.modules.products.application.mapper.ProductModelMapper;
import com.aram.erpcrud.modules.products.domain.ProductCategoryRepository;
import com.aram.erpcrud.modules.products.payload.ProductCategoryDTO;
import org.springframework.stereotype.Component;

@Component
public class GetAllProductCategories {

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductModelMapper productModelMapper;

    public GetAllProductCategories(
            ProductCategoryRepository productCategoryRepository,
            ProductModelMapper productModelMapper
    ) {
        this.productCategoryRepository = productCategoryRepository;
        this.productModelMapper = productModelMapper;
    }

    public Iterable<ProductCategoryDTO> get() {
        return productCategoryRepository.findAll().stream()
                .map(productModelMapper::toProductCategoryDTO)
                .toList();
    }

}
