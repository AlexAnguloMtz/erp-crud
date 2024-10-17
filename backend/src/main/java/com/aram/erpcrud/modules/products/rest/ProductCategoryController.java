package com.aram.erpcrud.modules.products.rest;

import com.aram.erpcrud.modules.products.application.ProductCategoryFacade;
import com.aram.erpcrud.modules.products.payload.*;
import com.aram.erpcrud.utils.PageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product-categories")
public class ProductCategoryController {

    private final ProductCategoryFacade productCategoryFacade;

    public ProductCategoryController(ProductCategoryFacade productCategoryFacade) {
        this.productCategoryFacade = productCategoryFacade;
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProductCategoryDTO>> getProductCategories(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @Min(0) Integer pageNumber,
            @RequestParam(required = false) @Min(1) @Max(50) Integer pageSize,
            @RequestParam(required = false) String sort
    ) {
        GetProductCategoriesQuery query = GetProductCategoriesQuery.builder()
                .search(search)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .sort(sort)
                .build();

        return new ResponseEntity<>(productCategoryFacade.getProductCategories(query), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<ProductCategoryDTO>> getAllProductCategories() {
        return new ResponseEntity<>(productCategoryFacade.getAllProductCategories(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createProductCategory(@Valid @RequestBody ProductCategoryCommand command) {
        productCategoryFacade.createProductCategory(command);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProductCategory(
            @PathVariable Long id,
            @Valid @RequestBody ProductCategoryCommand command
    ) {
        productCategoryFacade.updateProductCategory(id, command);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductCategoryById(@PathVariable("id") Long id) {
        productCategoryFacade.deleteProductCategoryById(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

}
