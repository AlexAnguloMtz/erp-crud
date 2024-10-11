package com.aram.erpcrud.modules.products;

import com.aram.erpcrud.modules.products.application.ProductFacade;
import com.aram.erpcrud.modules.products.payload.ProductDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SimpleProductDetailsService implements ProductDetailsService {

    private final ProductFacade productsFacade;

    public SimpleProductDetailsService(ProductFacade productsFacade) {
        this.productsFacade = productsFacade;
    }

    @Override
    public List<ProductDTO> getProductsByIds(List<Long> ids) {
        return productsFacade.getProductsByIds(ids);
    }

}