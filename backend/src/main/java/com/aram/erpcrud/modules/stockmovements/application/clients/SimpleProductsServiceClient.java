package com.aram.erpcrud.modules.stockmovements.application.clients;

import com.aram.erpcrud.modules.productdetails.ProductDetailsService;
import com.aram.erpcrud.modules.productdetails.payload.ProductDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class SimpleProductsServiceClient implements ProductServiceClient {

    private final ProductDetailsService productService;

    SimpleProductsServiceClient(ProductDetailsService productService) {
        this.productService = productService;
    }

    @Override
    public List<ProductDTO> getProducts(List<Long> ids) {
        return productService.getProductsByIds(ids);
    }

}