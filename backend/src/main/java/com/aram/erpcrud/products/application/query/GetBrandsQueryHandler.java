package com.aram.erpcrud.products.application.query;

import com.aram.erpcrud.common.PageResponse;
import com.aram.erpcrud.common.SafePagination;
import com.aram.erpcrud.products.domain.Brand;
import com.aram.erpcrud.products.domain.BrandRepository;
import com.aram.erpcrud.products.payload.BrandDTO;
import com.aram.erpcrud.products.payload.GetBrandsQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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

        Specification<Brand> specification = withSearchCriteria(query.search());

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

    private Specification<Brand> withSearchCriteria(String search) {
        return (Root<Brand> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (search == null || search.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String searchPattern = "%" + search.toLowerCase() + "%";
            Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern);
            return criteriaBuilder.or(namePredicate);
        };
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