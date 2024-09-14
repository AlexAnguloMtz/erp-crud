package com.aram.erpcrud.movements.application.clients;

import com.aram.erpcrud.products.ProductService;
import com.aram.erpcrud.products.payload.ProductDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class SimpleProductsServiceClient implements ProductServiceClient {

    private final ProductService productService;

    SimpleProductsServiceClient(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public List<ProductDTO> getProducts(List<String> ids) {
        return productService.getProductsByIds(ids);
    }

}