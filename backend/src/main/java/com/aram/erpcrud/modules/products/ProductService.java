package com.aram.erpcrud.modules.products;

import com.aram.erpcrud.modules.products.payload.ProductDTO;

import java.util.List;

public interface ProductService {

    List<ProductDTO> getProductsByIds(List<Long> ids);

}
