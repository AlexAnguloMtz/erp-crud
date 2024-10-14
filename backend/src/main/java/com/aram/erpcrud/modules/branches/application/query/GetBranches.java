package com.aram.erpcrud.modules.branches.application.query;

import com.aram.erpcrud.modules.branches.application.mapper.BranchModelMapper;
import com.aram.erpcrud.modules.branches.domain.Branch;
import com.aram.erpcrud.modules.branches.domain.BranchRepository;
import com.aram.erpcrud.modules.branches.payload.BranchDTO;
import com.aram.erpcrud.modules.branches.payload.GetBranchesQuery;
import com.aram.erpcrud.utils.PageResponse;
import com.aram.erpcrud.utils.SafePagination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class GetBranches {

    enum BranchSort {
        NameAsc("name", Sort.Direction.ASC),
        NameDesc("name", Sort.Direction.DESC),
        PhoneAsc("phone", Sort.Direction.ASC),
        PhoneDesc("phone", Sort.Direction.DESC),
        DistrictAsc("address.district", Sort.Direction.ASC),
        DistrictDesc("address.district", Sort.Direction.DESC),
        StreetAsc("address.street", Sort.Direction.ASC),
        StreetDesc("address.street", Sort.Direction.DESC),
        ZipCodeAsc("address.zipCode", Sort.Direction.ASC),
        ZipCodeDesc("address.zipCode", Sort.Direction.DESC),
        BranchTypeAsc("branchType.name", Sort.Direction.ASC),
        BranchTypeDesc("branchType.name", Sort.Direction.DESC);

        private final String field;
        private final Sort.Direction direction;

        BranchSort(String field, Sort.Direction direction) {
            this.field = field;
            this.direction = direction;
        }
    }

    private final SafePagination safePagination;
    private final BranchModelMapper branchModelMapper;
    private final BranchRepository branchRepository;

    public GetBranches(
            SafePagination safePagination,
            BranchModelMapper branchModelMapper,
            BranchRepository branchRepository
    ) {
        this.safePagination = safePagination;
        this.branchModelMapper = branchModelMapper;
        this.branchRepository = branchRepository;
    }

    public PageResponse<BranchDTO> handle(GetBranchesQuery query) {
        BranchSort branchSort = toBranchSort(query.sort());

        Sort sort = Sort.by(Sort.Order.by(branchSort.field).with(branchSort.direction));

        Pageable pageable = PageRequest.of(
                safePagination.safePageNumber(query.pageNumber()),
                safePagination.safePageSize(query.pageSize()),
                sort
        );

        Specification<Branch> specification = BranchSpecifications.withSearch(query.search());

        Page<Branch> page = branchRepository.findAll(specification, pageable);

        return new PageResponse<>(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast(),
                page.stream().map(branchModelMapper::toBranchDTO).toList()
        );
    }

    private BranchSort toBranchSort(String sort) {
        if ("name-desc".equals(sort)) {
            return BranchSort.NameDesc;
        }
        if ("phone-asc".equals(sort)) {
            return BranchSort.PhoneAsc;
        }
        if ("phone-desc".equals(sort)) {
            return BranchSort.PhoneDesc;
        }
        if ("district-asc".equals(sort)) {
            return BranchSort.DistrictAsc;
        }
        if ("district-desc".equals(sort)) {
            return BranchSort.DistrictDesc;
        }
        if ("street-asc".equals(sort)) {
            return BranchSort.StreetAsc;
        }
        if ("street-desc".equals(sort)) {
            return BranchSort.StreetDesc;
        }
        if ("zipCode-asc".equals(sort)) {
            return BranchSort.ZipCodeAsc;
        }
        if ("zipCode-desc".equals(sort)) {
            return BranchSort.ZipCodeDesc;
        }
        if ("branchType-asc".equals(sort)) {
            return BranchSort.BranchTypeAsc;
        }
        if ("branchType-desc".equals(sort)) {
            return BranchSort.BranchTypeDesc;
        }
        return BranchSort.NameAsc;
    }

}