package com.aram.erpcrud.modules.stockmovements.application.clients;

import com.aram.erpcrud.modules.productdetails.payload.ProductDTO;

import java.util.List;

public interface ProductServiceClient {
    List<ProductDTO> getProducts(List<Long> productIds);
}