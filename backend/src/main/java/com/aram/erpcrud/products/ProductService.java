package com.aram.erpcrud.products;

import com.aram.erpcrud.products.payload.ProductDTO;

import java.util.List;

public interface ProductService {

    List<ProductDTO> getProductsByIds(List<String> ids);

}
