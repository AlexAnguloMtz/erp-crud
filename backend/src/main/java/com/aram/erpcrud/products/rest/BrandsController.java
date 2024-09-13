package com.aram.erpcrud.products.rest;

import com.aram.erpcrud.common.PageResponse;
import com.aram.erpcrud.products.application.BrandFacade;
import com.aram.erpcrud.products.payload.BrandCommand;
import com.aram.erpcrud.products.payload.BrandDTO;
import com.aram.erpcrud.products.payload.GetBrandsQuery;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<Void> createBrand(@Valid @RequestBody BrandCommand command) {
        brandFacade.createBrand(command);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBrand(
            @Valid @RequestBody BrandCommand command,
            @PathVariable String id
    ) {
        brandFacade.updateBrand(id, command);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrandById(@PathVariable("id") String id) {
        brandFacade.deleteBrandById(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

}