package com.aram.erpcrud.modules.products.application.command;

import com.aram.erpcrud.modules.branches.application.command.UpdateBranch;
import com.aram.erpcrud.modules.products.application.ProductImageService;
import com.aram.erpcrud.modules.products.application.mapper.ProductModelMapper;
import com.aram.erpcrud.modules.products.domain.*;
import com.aram.erpcrud.modules.products.payload.UpdateProductCommand;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class UpdateProduct {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final BrandRepository brandRepository;
    private final InventoryUnitRepository inventoryUnitRepository;
    private final ProductImageService productImageService;

    public UpdateProduct(
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
    }

    public void handle(Long id, UpdateProductCommand command, MultipartFile image) {
        ValidImageAction validImageAction = ValidImageAction.makeOrThrow(command.imageAction(), image);

        Product product = findProductByIdOrThrow(id);
        checkUniqueSku(product, command.sku());
        
        Brand brand = findBrandByIdOrThrow(command.brandId());
        ProductCategory productCategory = findProductCategoryByIdOrThrow(command.productCategoryId());
        InventoryUnit inventoryUnit = findInventoryUnitByIdOrThrow(command.inventoryUnitId());

        String imageReference = executeProductImageAction(validImageAction, product.getImage());

        product.setBrand(brand);
        product.setProductCategory(productCategory);
        product.setInventoryUnit(inventoryUnit);
        product.setImage(imageReference);

        updateProductFields(product, command);

        productRepository.save(product);
    }

    private String executeProductImageAction(
            ValidImageAction imageAction,
            String imageReference
    ) {
        if (Objects.equals("edit", imageAction.action)) {
            return editBranchImage(imageReference, imageAction.imageFile);
        } else if (Objects.equals("delete", imageAction.action)) {
            deleteBranchImage(imageReference);
            return null;
        }
        return imageReference;
    }

    private String editBranchImage(String imageReference, MultipartFile imageFile) {
        if (StringUtils.hasText(imageReference)) {
            return productImageService.updateProductImage(imageReference, imageFile);
        } else {
            return productImageService.saveProductImage(imageFile);
        }
    }

    private void deleteBranchImage(String imageReference) {
        if (StringUtils.hasText(imageReference)) {
            productImageService.deleteProductImage(imageReference);
        }
    }

    private Brand findBrandByIdOrThrow(Long id) {
        return brandRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }

    private ProductCategory findProductCategoryByIdOrThrow(Long id) {
        return productCategoryRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }

    private InventoryUnit findInventoryUnitByIdOrThrow(Long id) {
        return inventoryUnitRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }

    private Product findProductByIdOrThrow(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }

    private void checkUniqueSku(Product product, String sku) {
        Optional<Product> bySkuOptional = productRepository.findBySku(sku);

        boolean skuConflict = bySkuOptional.isPresent() &&
                (!Objects.equals(product.getId(), bySkuOptional.get().getId()));

        if (skuConflict) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    private void updateProductFields(Product product, UpdateProductCommand command) {
        product.setName(command.name());
        product.setSku(command.sku());
        product.setSalePrice(command.salePrice());
    }

    private record ValidImageAction(String action, MultipartFile imageFile) {

        private static ValidImageAction makeOrThrow(String imageAction, MultipartFile image) {
            if (!List.of("none", "edit", "delete").contains(imageAction)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid image action: %s".formatted(imageAction));
            }

            if (!Objects.equals("edit", imageAction) && image != null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "If Product image file is present, action must be 'edit'. Got action: %s".formatted(imageAction)
                );
            }

            if (Objects.equals("edit", imageAction) && (image == null || image.isEmpty())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Can't edit Product image because image file is empty"
                );
            }

            return new ValidImageAction(imageAction, image);
        }
    }
}