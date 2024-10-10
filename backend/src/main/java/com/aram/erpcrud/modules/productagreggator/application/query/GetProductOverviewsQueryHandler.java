package com.aram.erpcrud.modules.productagreggator.application.query;

import com.aram.erpcrud.modules.productagreggator.payload.GetProductOverviewsQuery;
import com.aram.erpcrud.modules.productagreggator.payload.ProductOverviewDTO;
import org.springframework.stereotype.Component;

@Component
public class GetProductOverviewsQueryHandler {

    public Iterable<ProductOverviewDTO> handle(GetProductOverviewsQuery query) {
        return null;
    }

}