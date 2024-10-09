package com.aram.erpcrud.modules.products.application.query;

import com.aram.erpcrud.utils.PageResponse;
import com.aram.erpcrud.utils.SafePagination;
import com.aram.erpcrud.modules.products.domain.Brand;
import com.aram.erpcrud.modules.products.domain.BrandRepository;
import com.aram.erpcrud.modules.products.payload.BrandDTO;
import com.aram.erpcrud.modules.products.payload.GetBrandsQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class GetBrandsQueryHandler {

    enum BrandSort {
        NameAsc("name", Sort.Direction.ASC),
        NameDesc("name", Sort.Direction.DESC);

        private final String field;
        private final Sort.Direction direction;

        BrandSort(String field, Sort.Direction direction) {
            this.field = field;
            this.direction = direction;
        }
    }

    private final BrandRepository brandRepository;
    private final SafePagination safePagination;

    public GetBrandsQueryHandler(BrandRepository brandRepository, SafePagination safePagination) {
        this.brandRepository = brandRepository;
        this.safePagination = safePagination;
    }

    public PageResponse<BrandDTO> handle(GetBrandsQuery query) {
        BrandSort brandSort = toBrandSort(query.sort());

        Sort sort = Sort.by(Sort.Order.by(brandSort.field).with(brandSort.direction));

        Pageable pageable = PageRequest.of(
                safePagination.safePageNumber(query.pageNumber()),
                safePagination.safePageSize(query.pageSize()),
                sort
        );

        Specification<Brand> specification = BrandSpecifications.withSearch(query.search());

        Page<Brand> page = brandRepository.findAll(specification, pageable);

        return new PageResponse<>(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast(),
                page.stream().map(this::toBrandDto).toList()
        );
    }

    private BrandSort toBrandSort(String sort) {
        if ("name-desc".equals(sort)) {
            return BrandSort.NameDesc;
        }
        return BrandSort.NameAsc;
    }

    private BrandDTO toBrandDto(Brand brand) {
        return new BrandDTO(brand.getId(), brand.getName());
    }
}