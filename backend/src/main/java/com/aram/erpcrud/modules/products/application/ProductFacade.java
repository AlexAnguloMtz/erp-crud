package com.aram.erpcrud.modules.products.application;

import com.aram.erpcrud.modules.products.application.query.GetProductsByIdsQueryHandler;
import com.aram.erpcrud.modules.products.application.query.GetProductsQueryHandler;
import com.aram.erpcrud.modules.products.payload.GetProductsQuery;
import com.aram.erpcrud.modules.products.payload.ProductDTO;
import com.aram.erpcrud.utils.PageResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductFacade {

    private final GetProductsQueryHandler getProductsQueryHandler;
    private final GetProductsByIdsQueryHandler getProductsByIdsQueryHandler;

    public ProductFacade(
            GetProductsQueryHandler getProductsQueryHandler,
            GetProductsByIdsQueryHandler getProductsByIdsQueryHandler
    ) {
        this.getProductsQueryHandler = getProductsQueryHandler;
        this.getProductsByIdsQueryHandler = getProductsByIdsQueryHandler;
    }

    public List<ProductDTO> getProductsByIds(List<Long> ids) {
        return getProductsByIdsQueryHandler.handle(ids);
    }

    public PageResponse<ProductDTO> getProducts(GetProductsQuery query) {
        return getProductsQueryHandler.handle(query);
    }
}
