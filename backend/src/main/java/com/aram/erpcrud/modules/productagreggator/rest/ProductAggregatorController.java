package com.aram.erpcrud.modules.productagreggator.rest;

import com.aram.erpcrud.modules.productagreggator.application.ProductOverviewFacade;
import com.aram.erpcrud.modules.productagreggator.payload.GetProductOverviewsQuery;
import com.aram.erpcrud.modules.productagreggator.payload.ProductOverviewDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/product-overviews")
public class ProductAggregatorController {

    private final ProductOverviewFacade productOverviewFacade;

    public ProductAggregatorController(ProductOverviewFacade productOverviewFacade) {
        this.productOverviewFacade = productOverviewFacade;
    }

    @GetMapping
    public ResponseEntity<Iterable<ProductOverviewDTO>> getProductsOverviews(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @Min(0) Integer pageNumber,
            @RequestParam(required = false) @Min(1) @Max(50) Integer pageSize,
            @RequestParam(required = false) String sort
    ) {
        GetProductOverviewsQuery query = GetProductOverviewsQuery.builder()
                .search(search)
                .sort(sort)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .build();

        return new ResponseEntity<>(productOverviewFacade.handle(query), HttpStatus.OK);
    }

}