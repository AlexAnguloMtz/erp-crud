package com.aram.erpcrud.modules.products.application;

import com.aram.erpcrud.modules.products.application.command.CreateProduct;
import com.aram.erpcrud.modules.products.application.command.DeleteProductById;
import com.aram.erpcrud.modules.products.application.command.UpdateProduct;
import com.aram.erpcrud.modules.products.application.query.GetProductsByIds;
import com.aram.erpcrud.modules.products.application.query.GetProducts;
import com.aram.erpcrud.modules.products.payload.CreateProductCommand;
import com.aram.erpcrud.modules.products.payload.GetProductsQuery;
import com.aram.erpcrud.modules.products.payload.ProductDTO;
import com.aram.erpcrud.modules.products.payload.UpdateProductCommand;
import com.aram.erpcrud.utils.PageResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class ProductFacade {

    private final GetProducts getProductsQueryHandler;
    private final GetProductsByIds getProductsByIdsQueryHandler;
    private final CreateProduct createProduct;
    private final UpdateProduct updateProduct;
    private final DeleteProductById deleteProductById;

    public ProductFacade(
            GetProducts getProductsQueryHandler,
            GetProductsByIds getProductsByIdsQueryHandler,
            CreateProduct createProduct,
            UpdateProduct updateProduct,
            DeleteProductById deleteProductById
    ) {
        this.getProductsQueryHandler = getProductsQueryHandler;
        this.getProductsByIdsQueryHandler = getProductsByIdsQueryHandler;
        this.createProduct = createProduct;
        this.updateProduct = updateProduct;
        this.deleteProductById = deleteProductById;
    }

    public List<ProductDTO> getProductsByIds(List<Long> ids) {
        return getProductsByIdsQueryHandler.handle(ids);
    }

    public PageResponse<ProductDTO> getProducts(GetProductsQuery query) {
        return getProductsQueryHandler.handle(query);
    }

    public void createProduct(CreateProductCommand command, MultipartFile image) {
        createProduct.handle(command, image);
    }

    public void updateProduct(Long id, UpdateProductCommand command, MultipartFile image) {
        updateProduct.handle(id, command, image);
    }

    public void deleteProductById(Long id) {
        deleteProductById.handle(id);
    }
}
