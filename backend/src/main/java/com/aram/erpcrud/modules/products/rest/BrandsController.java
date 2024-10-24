package com.aram.erpcrud.modules.products.rest;

import com.aram.erpcrud.modules.products.payload.ProductCategoryDTO;
import com.aram.erpcrud.utils.PageResponse;
import com.aram.erpcrud.modules.products.application.BrandFacade;
import com.aram.erpcrud.modules.products.payload.BrandCommand;
import com.aram.erpcrud.modules.products.payload.BrandDTO;
import com.aram.erpcrud.modules.products.payload.GetBrandsQuery;
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
        GetBrandsQuery query = GetBrandsQuery.builder()
                .search(search)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .sort(sort)
                .build();

        return new ResponseEntity<>(brandFacade.getBrands(query), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<BrandDTO>> getAllBrands() {
        return new ResponseEntity<>(brandFacade.getAllBrands(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createBrand(@Valid @RequestBody BrandCommand command) {
        brandFacade.createBrand(command);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBrand(
            @PathVariable Long id,
            @Valid @RequestBody BrandCommand command
    ) {
        brandFacade.updateBrand(id, command);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrandById(@PathVariable("id") Long id) {
        brandFacade.deleteBrandById(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

}