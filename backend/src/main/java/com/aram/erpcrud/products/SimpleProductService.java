package com.aram.erpcrud.products;

import com.aram.erpcrud.products.application.ProductsFacade;
import com.aram.erpcrud.products.payload.ProductDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SimpleProductService implements ProductService {

    private final ProductsFacade productsFacade;

    public SimpleProductService(ProductsFacade productsFacade) {
        this.productsFacade = productsFacade;
    }

    @Override
    public List<ProductDTO> getProductsByIds(List<String> ids) {
        return productsFacade.getProductsByIds(ids);
    }
}
