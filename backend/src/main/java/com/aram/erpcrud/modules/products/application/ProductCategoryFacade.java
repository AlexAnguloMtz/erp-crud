package com.aram.erpcrud.modules.products.application;

import com.aram.erpcrud.modules.products.application.query.GetAllProductCategories;
import com.aram.erpcrud.modules.products.application.query.GetProductCategories;
import com.aram.erpcrud.modules.products.domain.ProductCategory;
import com.aram.erpcrud.modules.products.domain.ProductCategoryRepository;
import com.aram.erpcrud.modules.products.payload.GetProductCategoriesQuery;
import com.aram.erpcrud.modules.products.payload.ProductCategoryCommand;
import com.aram.erpcrud.modules.products.payload.ProductCategoryDTO;
import com.aram.erpcrud.utils.PageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;

@Component
public class ProductCategoryFacade {

    private final GetProductCategories getProductCategories;
    private final GetAllProductCategories getAllProductCategories;
    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryFacade(
            GetProductCategories getProductCategories,
            GetAllProductCategories getAllProductCategories,
            ProductCategoryRepository productCategoryRepository
    ) {
        this.getProductCategories = getProductCategories;
        this.getAllProductCategories = getAllProductCategories;
        this.productCategoryRepository = productCategoryRepository;
    }

    public PageResponse<ProductCategoryDTO> getProductCategories(GetProductCategoriesQuery query) {
        return getProductCategories.handle(query);
    }

    public Iterable<ProductCategoryDTO> getAllProductCategories() {
        return getAllProductCategories.get();
    }

    @Transactional
    public void createProductCategory(ProductCategoryCommand command) {
        Optional<ProductCategory> productCategoryOptional = productCategoryRepository.findByName(command.name());
        if (productCategoryOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        ProductCategory productCategory = new ProductCategory();
        productCategory.setName(command.name());

        productCategoryRepository.save(productCategory);
    }

    @Transactional
    public void updateProductCategory(Long id, ProductCategoryCommand command) {
        Optional<ProductCategory> byIdOptional = productCategoryRepository.findById(id);
        if (byIdOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        ProductCategory productCategory = byIdOptional.get();

        Optional<ProductCategory> byNameOptional = productCategoryRepository.findByName(command.name());

        boolean nameConflict = byNameOptional.isPresent() &&
                (!Objects.equals(productCategory.getId(), byNameOptional.get().getId()));

        if (nameConflict) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        productCategory.setName(command.name());

        productCategoryRepository.save(productCategory);
    }

    @Transactional
    public void deleteProductCategoryById(Long id) {
        productCategoryRepository.deleteById(id);
    }


}
