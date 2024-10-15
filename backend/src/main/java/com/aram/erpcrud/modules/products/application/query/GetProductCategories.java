package com.aram.erpcrud.modules.products.application.query;

import com.aram.erpcrud.modules.products.application.mapper.ProductModelMapper;
import com.aram.erpcrud.modules.products.domain.ProductCategory;
import com.aram.erpcrud.modules.products.domain.ProductCategoryRepository;
import com.aram.erpcrud.modules.products.payload.GetProductCategoriesQuery;
import com.aram.erpcrud.modules.products.payload.ProductCategoryDTO;
import com.aram.erpcrud.utils.PageResponse;
import com.aram.erpcrud.utils.SafePagination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class GetProductCategories {

    enum ProductCategorySort {
        NameAsc("name", Sort.Direction.ASC),
        NameDesc("name", Sort.Direction.DESC);

        private final String field;
        private final Sort.Direction direction;

        ProductCategorySort(String field, Sort.Direction direction) {
            this.field = field;
            this.direction = direction;
        }
    }

    private final ProductCategoryRepository productCategoryRepository;
    private final SafePagination safePagination;
    private final ProductModelMapper productModelMapper;

    public GetProductCategories(
            ProductCategoryRepository productCategoryRepository,
            SafePagination safePagination,
            ProductModelMapper productModelMapper
    ) {
        this.productCategoryRepository = productCategoryRepository;
        this.safePagination = safePagination;
        this.productModelMapper = productModelMapper;
    }

    public PageResponse<ProductCategoryDTO> handle(GetProductCategoriesQuery query) {
        ProductCategorySort productCategorySort = toProductCategorySort(query.sort());

        Sort sort = Sort.by(Sort.Order.by(productCategorySort.field).with(productCategorySort.direction));

        Pageable pageable = PageRequest.of(
                safePagination.safePageNumber(query.pageNumber()),
                safePagination.safePageSize(query.pageSize()),
                sort
        );

        Specification<ProductCategory> specification = ProductCategorySpecifications.withSearch(query.search());

        Page<ProductCategory> page = productCategoryRepository.findAll(specification, pageable);

        return new PageResponse<>(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast(),
                page.stream().map(productModelMapper::toProductCategoryDTO).toList()
        );
    }

    private ProductCategorySort toProductCategorySort(String sort) {
        if ("name-desc".equals(sort)) {
            return ProductCategorySort.NameDesc;
        }
        return ProductCategorySort.NameAsc;
    }

}