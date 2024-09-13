package com.aram.erpcrud.products.rest;

import com.aram.erpcrud.common.PageResponse;
import com.aram.erpcrud.products.application.BrandFacade;
import com.aram.erpcrud.products.payload.BrandDTO;
import com.aram.erpcrud.products.payload.GetBrandsQuery;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/brands")
public class BrandsController {

    private final BrandFacade brandFacade;

    public BrandsController(BrandFacade brandFacade) {
        this.brandFacade = brandFacade;
    }

    @GetMapping
    public ResponseEntity<PageResponse<BrandDTO>> getBrands(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @Min(0) Integer pageNumber,
            @RequestParam(required = false) @Min(1) @Max(50) Integer pageSize,
            @RequestParam(required = false) String sort
    ) {
        GetBrandsQuery getBrandsQuery = new GetBrandsQuery(search, pageNumber, pageSize, sort);
        return new ResponseEntity<>(brandFacade.getBrands(getBrandsQuery), HttpStatus.OK);
    }
}