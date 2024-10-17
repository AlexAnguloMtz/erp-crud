package com.aram.erpcrud.modules.products.application.command;

import com.aram.erpcrud.modules.products.application.ProductImageService;
import com.aram.erpcrud.modules.products.application.mapper.ProductModelMapper;
import com.aram.erpcrud.modules.products.domain.*;
import com.aram.erpcrud.modules.products.payload.CreateProductCommand;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class CreateProduct {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final BrandRepository brandRepository;
    private final InventoryUnitRepository inventoryUnitRepository;
    private final ProductImageService productImageService;
    private final ProductModelMapper productModelMapper;

    public CreateProduct(
            ProductRepository productRepository,
            ProductCategoryRepository productCategoryRepository,
            BrandRepository brandRepository,
            InventoryUnitRepository inventoryUnitRepository,
            ProductImageService productImageService,
            ProductModelMapper productModelMapper
    ) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.brandRepository = brandRepository;
        this.inventoryUnitRepository = inventoryUnitRepository;
        this.productImageService = productImageService;
        this.productModelMapper = productModelMapper;
    }

    @Transactional
    public void handle(CreateProductCommand command, MultipartFile image) {
        Optional<Product> productBySkuOptional = productRepository.findBySku(command.sku());
        if (productBySkuOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        Optional<Brand> brandOptional = brandRepository.findById(command.brandId());
        if (brandOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Optional<ProductCategory> productCategoryOptional = productCategoryRepository.findById(command.productCategoryId());
        if (productCategoryOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Optional<InventoryUnit> inventoryUnitOptional = inventoryUnitRepository.findById(command.inventoryUnitId());
        if (inventoryUnitOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Product product = productModelMapper.toProduct(
                command,
                brandOptional.get(),
                productCategoryOptional.get(),
                inventoryUnitOptional.get()
        );

        if (image != null && !image.isEmpty()) {
            String imagePath = productImageService.saveProductImage(image);
            product.setImage(imagePath);
        }

        productRepository.save(product);
    }

}