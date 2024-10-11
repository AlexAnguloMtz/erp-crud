package com.aram.erpcrud.modules.products;

import com.aram.erpcrud.modules.products.payload.ProductDTO;

import java.util.List;

public interface ProductDetailsService {

    List<ProductDTO> getProductsByIds(List<Long> ids);

}