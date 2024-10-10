package com.aram.erpcrud.modules.productdetails;

import com.aram.erpcrud.modules.productdetails.payload.ProductDTO;

import java.util.List;

public interface ProductDetailsService {

    List<ProductDTO> getProductsByIds(List<Long> ids);

}