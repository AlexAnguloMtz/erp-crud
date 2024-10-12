package com.aram.erpcrud.modules.branches.application.command;

import com.aram.erpcrud.modules.branches.domain.BranchTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class DeleteBranchTypeByIdCommandHandler {

    private final BranchTypeRepository branchTypeRepository;

    public DeleteBranchTypeByIdCommandHandler(BranchTypeRepository branchTypeRepository) {
        this.branchTypeRepository = branchTypeRepository;
    }

    @Transactional
    public void handle(Long id) {
        branchTypeRepository.deleteById(id);
    }

}