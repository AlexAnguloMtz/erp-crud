package com.aram.erpcrud.modules.productagreggator.application;

import com.aram.erpcrud.modules.productagreggator.application.query.GetProductOverviewsQueryHandler;
import com.aram.erpcrud.modules.productagreggator.payload.GetProductOverviewsQuery;
import com.aram.erpcrud.modules.productagreggator.payload.ProductOverviewDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductOverviewFacade {

    private final GetProductOverviewsQueryHandler getProductOverviewsQueryHandler;

    public ProductOverviewFacade(
            GetProductOverviewsQueryHandler getProductOverviewsQueryHandler
    ) {
        this.getProductOverviewsQueryHandler = getProductOverviewsQueryHandler;
    }

    public Iterable<ProductOverviewDTO> handle(GetProductOverviewsQuery query) {
        return getProductOverviewsQueryHandler.handle(query);
    }
}