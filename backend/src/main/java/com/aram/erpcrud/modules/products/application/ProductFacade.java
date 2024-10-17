package com.aram.erpcrud.modules.products.application;

import com.aram.erpcrud.modules.products.application.command.CreateProduct;
import com.aram.erpcrud.modules.products.application.query.GetProductsByIds;
import com.aram.erpcrud.modules.products.application.query.GetProducts;
import com.aram.erpcrud.modules.products.payload.CreateProductCommand;
import com.aram.erpcrud.modules.products.payload.GetProductsQuery;
import com.aram.erpcrud.modules.products.payload.ProductDTO;
import com.aram.erpcrud.utils.PageResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class ProductFacade {

    private final GetProducts getProductsQueryHandler;
    private final GetProductsByIds getProductsByIdsQueryHandler;
    private final CreateProduct createProduct;

    public ProductFacade(
            GetProducts getProductsQueryHandler,
            GetProductsByIds getProductsByIdsQueryHandler,
            CreateProduct createProduct
    ) {
        this.getProductsQueryHandler = getProductsQueryHandler;
        this.getProductsByIdsQueryHandler = getProductsByIdsQueryHandler;
        this.createProduct = createProduct;
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
}
