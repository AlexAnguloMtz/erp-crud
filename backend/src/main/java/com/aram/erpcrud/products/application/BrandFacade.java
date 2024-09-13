package com.aram.erpcrud.products.application;

import com.aram.erpcrud.common.PageResponse;
import com.aram.erpcrud.products.application.query.GetBrands;
import com.aram.erpcrud.products.payload.BrandDTO;
import com.aram.erpcrud.products.payload.GetBrandsQuery;
import org.springframework.stereotype.Component;

@Component
public class BrandFacade {

    private final GetBrands getBrands;

    public BrandFacade(GetBrands getBrands) {
        this.getBrands = getBrands;
    }

    public PageResponse<BrandDTO> getBrands(GetBrandsQuery query) {
        return getBrands.handle(query);
    }
}
