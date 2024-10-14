package com.aram.erpcrud.modules.products.application;

import com.aram.erpcrud.modules.products.application.query.GetProductsByIds;
import com.aram.erpcrud.modules.products.application.query.GetProducts;
import com.aram.erpcrud.modules.products.payload.GetProductsQuery;
import com.aram.erpcrud.modules.products.payload.ProductDTO;
import com.aram.erpcrud.utils.PageResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductFacade {

    private final GetProducts getProductsQueryHandler;
    private final GetProductsByIds getProductsByIdsQueryHandler;

    public ProductFacade(
            GetProducts getProductsQueryHandler,
            GetProductsByIds getProductsByIdsQueryHandler
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
