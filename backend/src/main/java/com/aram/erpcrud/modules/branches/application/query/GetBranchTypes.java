package com.aram.erpcrud.modules.branches.application.query;

import com.aram.erpcrud.modules.branches.application.mapper.BranchModelMapper;
import com.aram.erpcrud.modules.branches.domain.BranchType;
import com.aram.erpcrud.modules.branches.domain.BranchTypeRepository;
import com.aram.erpcrud.modules.branches.payload.BranchTypeDTO;
import com.aram.erpcrud.modules.branches.payload.GetBranchTypesQuery;
import com.aram.erpcrud.utils.PageResponse;
import com.aram.erpcrud.utils.SafePagination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class GetBranchTypes {

    enum BranchTypeSort {
        NameAsc("name", Sort.Direction.ASC),
        NameDesc("name", Sort.Direction.DESC),

        DescriptionAsc("description", Sort.Direction.ASC),
        DescriptionDesc("description", Sort.Direction.DESC);

        private final String field;
        private final Sort.Direction direction;

        BranchTypeSort(String field, Sort.Direction direction) {
            this.field = field;
            this.direction = direction;
        }
    }

    private final SafePagination safePagination;
    private final BranchModelMapper branchModelMapper;
    private final BranchTypeRepository branchTypeRepository;

    public GetBranchTypes(
            SafePagination safePagination,
            BranchModelMapper branchModelMapper,
            BranchTypeRepository branchTypeRepository
    ) {
        this.safePagination = safePagination;
        this.branchModelMapper = branchModelMapper;
        this.branchTypeRepository = branchTypeRepository;
    }

    public PageResponse<BranchTypeDTO> handle(GetBranchTypesQuery query) {
        BranchTypeSort branchTypeSort = toBranchTypeSort(query.sort());

        Sort sort = Sort.by(Sort.Order.by(branchTypeSort.field).with(branchTypeSort.direction));

        Pageable pageable = PageRequest.of(
                safePagination.safePageNumber(query.pageNumber()),
                safePagination.safePageSize(query.pageSize()),
                sort
        );

        Specification<BranchType> specification = BranchTypeSpecifications.withSearch(query.search());

        Page<BranchType> page = branchTypeRepository.findAll(specification, pageable);

        return new PageResponse<>(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast(),
                page.stream().map(branchModelMapper::toBranchTypeDTO).toList()
        );
    }

    private BranchTypeSort toBranchTypeSort(String sort) {
        if ("name-desc".equals(sort)) {
            return BranchTypeSort.NameDesc;
        }
        if ("description-asc".equals(sort)) {
            return BranchTypeSort.DescriptionAsc;
        }
        if ("description-desc".equals(sort)) {
            return BranchTypeSort.DescriptionDesc;
        }
        return BranchTypeSort.NameAsc;
    }
}