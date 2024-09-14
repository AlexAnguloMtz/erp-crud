package com.aram.erpcrud.products.application;

import com.aram.erpcrud.products.application.query.GetProductsByIdsQueryHandler;
import com.aram.erpcrud.products.payload.ProductDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductsFacade {

    private final GetProductsByIdsQueryHandler getProductsByIdsQueryHandler;

    public ProductsFacade(GetProductsByIdsQueryHandler getProductsByIdsQueryHandler) {
        this.getProductsByIdsQueryHandler = getProductsByIdsQueryHandler;
    }

    public List<ProductDTO> getProductsByIds(List<String> ids) {
        return getProductsByIdsQueryHandler.handle(ids);
    }

}
