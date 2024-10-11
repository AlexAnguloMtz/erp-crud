package com.aram.erpcrud.modules.products.application.query;

import com.aram.erpcrud.modules.products.application.mapper.ProductModelMapper;
import com.aram.erpcrud.modules.products.domain.ProductRepository;
import com.aram.erpcrud.modules.products.payload.ProductDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetProductsByIdsQueryHandler {

    private final ProductRepository productRepository;
    private final ProductModelMapper productModelMapper;

    public GetProductsByIdsQueryHandler(ProductRepository productRepository, ProductModelMapper productModelMapper) {
        this.productRepository = productRepository;
        this.productModelMapper = productModelMapper;
    }

    public List<ProductDTO> handle(List<Long> ids) {
        return productRepository.findAllById(ids).stream()
                .map(productModelMapper::toProductDTO)
                .toList();
    }

}
