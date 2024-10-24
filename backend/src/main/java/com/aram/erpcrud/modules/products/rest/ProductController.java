package com.aram.erpcrud.modules.products.rest;

import com.aram.erpcrud.modules.products.application.ProductFacade;
import com.aram.erpcrud.modules.products.application.ProductImageService;
import com.aram.erpcrud.modules.products.payload.CreateProductCommand;
import com.aram.erpcrud.modules.products.payload.GetProductsQuery;
import com.aram.erpcrud.modules.products.payload.ProductDTO;
import com.aram.erpcrud.modules.products.payload.UpdateProductCommand;
import com.aram.erpcrud.utils.PageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductFacade productsFacade;
    private final ProductImageService productImageService;

    public ProductController(
            ProductFacade productsFacade,
            ProductImageService productImageService
    ) {
        this.productsFacade = productsFacade;
        this.productImageService = productImageService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProductDTO>> getProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @Min(0) Integer pageNumber,
            @RequestParam(required = false) @Min(1) @Max(50) Integer pageSize,
            @RequestParam(required = false) String sort
    ) {
        GetProductsQuery query = GetProductsQuery.builder()
                .search(search)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .sort(sort)
                .build();

        return new ResponseEntity<>(productsFacade.getProducts(query), HttpStatus.OK);
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Void> createProduct(
            @RequestPart CreateProductCommand command,
            @RequestParam(required = false) MultipartFile image)
    {
        productsFacade.createProduct(command, image);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Void> updateProduct(
            @PathVariable Long id,
            @Valid @RequestPart UpdateProductCommand command,
            @RequestPart(required = false) MultipartFile image
    ) {
        productsFacade.updateProduct(id, command, image);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable("id") Long id) {
        productsFacade.deleteProductById(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/product-image/{image}", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
    public ResponseEntity<byte[]> getProductImage(@PathVariable String image) {
        return new ResponseEntity<>(productImageService.getProductImage(image), HttpStatus.OK);
    }

}