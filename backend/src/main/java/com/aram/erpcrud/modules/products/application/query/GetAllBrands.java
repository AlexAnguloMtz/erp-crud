package com.aram.erpcrud.modules.products.application.query;

import com.aram.erpcrud.modules.products.application.mapper.ProductModelMapper;
import com.aram.erpcrud.modules.products.domain.BrandRepository;
import com.aram.erpcrud.modules.products.payload.BrandDTO;
import org.springframework.stereotype.Component;

@Component
public class GetAllBrands {

    private final BrandRepository brandRepository;
    private final ProductModelMapper productModelMapper;

    public GetAllBrands(
            BrandRepository brandRepository,
            ProductModelMapper productModelMapper
    ) {
        this.brandRepository = brandRepository;
        this.productModelMapper = productModelMapper;
    }

    public Iterable<BrandDTO> get() {
        return brandRepository.findAll().stream()
                .map(productModelMapper::toBrandDTO)
                .toList();
    }

}
