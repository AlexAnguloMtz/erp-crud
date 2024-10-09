package com.aram.erpcrud.modules.stockmovements.application.clients;

import com.aram.erpcrud.modules.products.ProductService;
import com.aram.erpcrud.modules.products.payload.ProductDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class SimpleProductsServiceClient implements ProductServiceClient {

    private final ProductService productService;

    SimpleProductsServiceClient(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public List<ProductDTO> getProducts(List<Long> ids) {
        return productService.getProductsByIds(ids);
    }

}