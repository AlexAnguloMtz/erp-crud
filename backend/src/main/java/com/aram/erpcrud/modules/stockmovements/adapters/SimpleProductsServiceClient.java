package com.aram.erpcrud.modules.stockmovements.adapters;

import com.aram.erpcrud.modules.products.ProductDetailsService;
import com.aram.erpcrud.modules.products.payload.ProductDTO;
import com.aram.erpcrud.modules.stockmovements.application.clients.ProductServiceClient;
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