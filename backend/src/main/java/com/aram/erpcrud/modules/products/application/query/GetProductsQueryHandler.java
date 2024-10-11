package com.aram.erpcrud.modules.products.application.query;

import com.aram.erpcrud.modules.products.application.mapper.ProductModelMapper;
import com.aram.erpcrud.modules.products.domain.Product;
import com.aram.erpcrud.modules.products.domain.ProductRepository;
import com.aram.erpcrud.modules.products.payload.GetProductsQuery;
import com.aram.erpcrud.modules.products.payload.ProductDTO;
import com.aram.erpcrud.utils.PageResponse;
import com.aram.erpcrud.utils.SafePagination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class GetProductsQueryHandler {

    enum ProductSort {
        NameAsc("name", Sort.Direction.ASC),
        NameDesc("name", Sort.Direction.DESC),
        BrandAsc("brand.name", Sort.Direction.ASC),
        BrandDesc("brand.name", Sort.Direction.DESC),
        ProductCategoryAsc("productCategory.name", Sort.Direction.ASC),
        ProductCategoryDesc("productCategory.name", Sort.Direction.DESC);

        private final String field;
        private final Sort.Direction direction;

        ProductSort(String field, Sort.Direction direction) {
            this.field = field;
            this.direction = direction;
        }
    }

    private final ProductRepository productRepository;
    private final SafePagination safePagination;
    private final ProductModelMapper productModelMapper;

    public GetProductsQueryHandler(
            ProductRepository productRepository,
            SafePagination safePagination,
            ProductModelMapper productModelMapper
    ) {
        this.productRepository = productRepository;
        this.safePagination = safePagination;
        this.productModelMapper = productModelMapper;
    }

    public PageResponse<ProductDTO> handle(GetProductsQuery query) {
        ProductSort productSort = toProductSort(query.sort());

        Sort sort = Sort.by(Sort.Order.by(productSort.field).with(productSort.direction));

        Pageable pageable = PageRequest.of(
                safePagination.safePageNumber(query.pageNumber()),
                safePagination.safePageSize(query.pageSize()),
                sort
        );

        Specification<Product> specification = ProductSpecifications.withSearch(query.search());

        Page<Product> page = productRepository.findAll(specification, pageable);

        return new PageResponse<>(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast(),
                page.stream().map(productModelMapper::toProductDTO).toList()
        );
    }

    private ProductSort toProductSort(String sort) {
        if ("name-desc".equals(sort)) {
            return ProductSort.NameDesc;
        }
        if ("brand-asc".equals(sort)) {
            return ProductSort.BrandAsc;
        }
        if ("brand-desc".equals(sort)) {
            return ProductSort.BrandDesc;
        }
        if ("productCategory-asc".equals(sort)) {
            return ProductSort.ProductCategoryAsc;
        }
        if ("productCategory-desc".equals(sort)) {
            return ProductSort.ProductCategoryDesc;
        }
        return ProductSort.NameAsc;
    }
}
