package com.aram.erpcrud.products;

import com.aram.erpcrud.products.payload.ProductDTO;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    List<ProductDTO> getProductsByIds(List<UUID> ids);

}
