package com.aram.erpcrud.modules.products.application;

import com.aram.erpcrud.modules.products.application.query.GetProductsByIdsQueryHandler;
import com.aram.erpcrud.modules.products.payload.ProductDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductsFacade {

    private final GetProductsByIdsQueryHandler getProductsByIdsQueryHandler;

    public ProductsFacade(GetProductsByIdsQueryHandler getProductsByIdsQueryHandler) {
        this.getProductsByIdsQueryHandler = getProductsByIdsQueryHandler;
    }

    public List<ProductDTO> getProductsByIds(List<Long> ids) {
        return getProductsByIdsQueryHandler.handle(ids);
    }

}
