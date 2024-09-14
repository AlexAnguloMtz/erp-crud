package com.aram.erpcrud.movements.application.clients;

import com.aram.erpcrud.products.payload.ProductDTO;

import java.util.List;

public interface ProductServiceClient {
    List<ProductDTO> getProducts(List<String> productIds);
}
