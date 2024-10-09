package com.aram.erpcrud.modules.products;

import com.aram.erpcrud.modules.products.application.ProductsFacade;
import com.aram.erpcrud.modules.products.payload.ProductDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SimpleProductService implements ProductService {

    private final ProductsFacade productsFacade;

    public SimpleProductService(ProductsFacade productsFacade) {
        this.productsFacade = productsFacade;
    }

    @Override
    public List<ProductDTO> getProductsByIds(List<Long> ids) {
        return productsFacade.getProductsByIds(ids);
    }
}
