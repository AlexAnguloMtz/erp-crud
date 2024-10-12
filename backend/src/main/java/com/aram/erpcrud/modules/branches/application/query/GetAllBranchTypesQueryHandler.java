package com.aram.erpcrud.modules.branches.application.query;

import com.aram.erpcrud.modules.branches.application.mapper.BranchModelMapper;
import com.aram.erpcrud.modules.branches.domain.BranchTypeRepository;
import com.aram.erpcrud.modules.branches.payload.BranchTypeDTO;
import org.springframework.stereotype.Component;

@Component
public class GetAllBranchTypesQueryHandler {

    private final BranchModelMapper branchModelMapper;
    private final BranchTypeRepository branchTypeRepository;

    public GetAllBranchTypesQueryHandler(
            BranchModelMapper branchModelMapper,
            BranchTypeRepository branchTypeRepository
    ) {
        this.branchModelMapper = branchModelMapper;
        this.branchTypeRepository = branchTypeRepository;
    }

    public Iterable<BranchTypeDTO> get() {
        return branchTypeRepository.findAllByOrderByNameAsc().stream()
                .map(branchModelMapper::toBranchTypeDTO)
                .toList();
    }
}