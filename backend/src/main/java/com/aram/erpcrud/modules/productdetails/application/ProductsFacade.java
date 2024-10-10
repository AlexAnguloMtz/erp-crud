package com.aram.erpcrud.modules.productdetails.application;

import com.aram.erpcrud.modules.productdetails.application.query.GetProductsByIdsQueryHandler;
import com.aram.erpcrud.modules.productdetails.payload.ProductDTO;
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
