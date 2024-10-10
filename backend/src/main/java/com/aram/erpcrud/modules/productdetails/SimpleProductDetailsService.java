package com.aram.erpcrud.modules.productdetails;

import com.aram.erpcrud.modules.productagreggator.payload.GetProductOverviewsQuery;
import com.aram.erpcrud.modules.productdetails.application.ProductsFacade;
import com.aram.erpcrud.modules.productdetails.payload.ProductDTO;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class SimpleProductDetailsService implements ProductDetailsService {

    private final ProductsFacade productsFacade;

    public SimpleProductDetailsService(ProductsFacade productsFacade) {
        this.productsFacade = productsFacade;
    }

    @Override
    public List<ProductDTO> getProductsByIds(List<Long> ids) {
        return productsFacade.getProductsByIds(ids);
    }

}